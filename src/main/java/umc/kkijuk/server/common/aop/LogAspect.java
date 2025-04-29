package umc.kkijuk.server.common.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


@Aspect
@Slf4j
@Component
public class LogAspect {

    @Pointcut("execution(* umc.kkijuk.server..controller..*(..))")
    public void controllerMethods(){}

    @Pointcut("execution(* umc.kkijuk.server..service..*(..))")
    public void serviceMethods(){}


    @Around("controllerMethods()")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = attributes.getRequest();
        if (request == null) {
            return joinPoint.proceed();
        }

        String controllerClass = joinPoint.getSignature().getDeclaringType().getName();
        String methodName = joinPoint.getSignature().getName();
        Map<String, Object> params = new HashMap<>();

        try{
            String decoder = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);

            params.put("controller", controllerClass);
            params.put("method", methodName);
            params.put("params", getParams(request));
            params.put("request_uri", decoder);
            params.put("http_method", request.getMethod());

        }catch (Exception e){
            log.error("Exception in Controller Method {}.{}: {}", controllerClass, methodName, e.getMessage());
            throw e;
        }

        log.info("[{}] {}", params.get("http_method"), params.get("request_uri"));
        log.info("method: {}.{}", params.get("controller") ,params.get("method"));
        log.info("params: {}", params.get("params"));

        Object result = joinPoint.proceed();

        return result;


    }
    @Around("serviceMethods()")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String serviceClass = joinPoint.getSignature().getDeclaringTypeName();
        String serviceMethod = joinPoint.getSignature().getName();

        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.info("[Service] {}.{} executed in {} ms", serviceClass, serviceMethod, elapsedTime);
            return result;

        } catch (Throwable e) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.error("Exception in Service Method {}.{} after {} ms: {}", serviceClass, serviceMethod, elapsedTime, e.getMessage());
            throw e;

        }

    }
    private static JSONObject getParams(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();

        // 쿼리 파라미터 수집
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String param = paramNames.nextElement();
            String replacedParam = param.replaceAll("\\.", "-");
            jsonObject.put(replacedParam, request.getParameter(param));
        }

        // PathVariable 수집
        @SuppressWarnings("unchecked")
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (pathVariables != null) {
            for (Map.Entry<String, String> entry : pathVariables.entrySet()) {
                String key = entry.getKey().replaceAll("\\.", "-");
                jsonObject.put(key, entry.getValue());
            }
        }

        return jsonObject;
    }


}
