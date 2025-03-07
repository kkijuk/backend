package umc.kkijuk.server.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class ExceptionDiscordLoggingAspect {
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${discord.webhook.error-url:#{null}}")
    private  String discordWebhookUrl;

    public ExceptionDiscordLoggingAspect(RestTemplateBuilder restTemplateBuilder,
                                  ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    /**
     * 디스코드 알림에서 제외할 예외를 판별
     * ExceptionControllerAdvice 에서 처리하는 예외들은 제외
     * @SkipDiscordNotification 이 붙거나, 따로 처리해준 예외인 경우 알림을 보내지 않음
     */
    private boolean shouldSkipNotification(Throwable ex) {
        if (ex instanceof org.springframework.web.bind.MethodArgumentNotValidException ||
                ex instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException) {
            return true;
        }

        if (ex.getClass().isAnnotationPresent(SkipDiscordNotification.class)) {
            return true;
        }

        return false;
    }

    @AfterThrowing(
            pointcut = "within(umc.kkijuk..*) && !within(umc.kkijuk.server.auth.jwt..*)",
            throwing = "ex"
    )
    public void handleException(JoinPoint joinPoint, Throwable ex) {
        if (shouldSkipNotification(ex)) {
            log.warn("Skipping Discord notification for exception: {}", ex.getClass().getSimpleName());
            return;
        }

        try {
            // 1) Embed 필드 구성
            List<Map<String, Object>> fields = buildFields(joinPoint, ex);

            // 2) Embed 객체 구성
            Map<String, Object> embed = buildEmbed(fields);

            // 3) 최종 Payload(JSON) 구성
            Map<String, Object> payload = buildPayload(embed);

            // 4) JSON 변환
            JsonNode jsonNode = objectMapper.convertValue(payload, JsonNode.class);
            String payloadJson = jsonNode.toPrettyString();

            // 5) 스택 트레이스 문자열 생성
            String fullStackTrace = buildStackTrace(ex);

            // 6) 비동기적으로 디스코드 전송
            sendToDiscord(payloadJson, fullStackTrace);
        } catch (Exception e) {
            log.warn("Discord notification failed: {}", e.getMessage());
        }
    }

    private List<Map<String, Object>> buildFields(JoinPoint joinPoint, Throwable ex) {
        Map<String, Object> locationField = new LinkedHashMap<>();
        locationField.put("name", "Location");
        locationField.put("value", joinPoint.getSignature().getDeclaringTypeName());
        locationField.put("inline", false);

        Map<String, Object> exceptionField = new LinkedHashMap<>();
        exceptionField.put("name", "Exception Type");
        exceptionField.put("value", ex.getClass().getName());
        exceptionField.put("inline", false);

        Map<String, Object> messageField = new LinkedHashMap<>();
        messageField.put("name", "Message");
        messageField.put("value", ex.getMessage() != null ? ex.getMessage() : "No message");
        messageField.put("inline", false);

        Map<String, Object> timeField = new LinkedHashMap<>();
        timeField.put("name", "Time");
        timeField.put("value", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        timeField.put("inline", false);

        List<Map<String, Object>> fields = new ArrayList<>();
        fields.add(locationField);
        fields.add(exceptionField);
        fields.add(messageField);
        fields.add(timeField);

        return fields;
    }

    private Map<String, Object> buildEmbed(List<Map<String, Object>> fields) {
        Map<String, Object> embed = new LinkedHashMap<>();
        embed.put("title", "Exception Occurred");
        embed.put("color", 16711680); // 빨간색 (#FF0000)
        embed.put("fields", fields);
        return embed;
    }

    private Map<String, Object> buildPayload(Map<String, Object> embed) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("embeds", Collections.singletonList(embed));
        return payload;
    }

    private String buildStackTrace(Throwable ex) {
        return Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));
    }

    /**
     * multipart/form-data 파일(스택 트레이스) + JSON 메시지를 함께 전송
     * 비동기적 처리
     */
    @Async
    protected void sendToDiscord(String payloadJson, String fullStackTrace) {
        byte[] stackTraceBytes = fullStackTrace.getBytes(StandardCharsets.UTF_8);
        ByteArrayResource stackTraceResource = new ByteArrayResource(stackTraceBytes) {
            @Override
            public String getFilename() {
                return "stacktrace.txt";
            }
        };

        LinkedMultiValueMap<String, Object> multipartBody = new LinkedMultiValueMap<>();
        multipartBody.add("payload_json", payloadJson);
        multipartBody.add("file", stackTraceResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(multipartBody, headers);

        restTemplate.postForEntity(discordWebhookUrl, requestEntity, String.class);
    }
}
