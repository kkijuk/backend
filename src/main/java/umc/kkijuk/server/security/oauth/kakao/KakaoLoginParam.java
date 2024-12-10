//package umc.kkijuk.server.security.oauth.kakao;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//public class KakaoLoginParam {
//    private String authorizationCode;
//
//    public MultiValueMap<String, String> makeBody() {
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("code", authorizationCode);
//
//        return body;
//    }
//}
//
