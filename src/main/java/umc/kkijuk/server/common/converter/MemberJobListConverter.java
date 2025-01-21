package umc.kkijuk.server.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import org.springframework.stereotype.Component;
import umc.kkijuk.server.member.domain.MemberJob;

import java.io.IOException;
import java.util.List;

@Component
public class MemberJobListConverter implements AttributeConverter<List<MemberJob>, String> {
    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

    @Override
    public String convertToDatabaseColumn(List<MemberJob> attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("MemberJob 리스트를 JSON 문자열로 변환하는 데 실패했습니다.", e);
        }
    }

    @Override
    public List<MemberJob> convertToEntityAttribute(String dbData) {
        try {
            return mapper.readValue(dbData, new TypeReference<List<MemberJob>>() {});
        } catch (IOException e) {
            throw new IllegalArgumentException("데이터베이스 JSON 문자열을 MemberJob 리스트로 변환하는 데 실패했습니다.", e);
        }
    }
}
