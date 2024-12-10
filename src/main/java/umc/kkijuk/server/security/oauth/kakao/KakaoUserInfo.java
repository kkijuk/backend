//package umc.kkijuk.server.security.oauth.kakao;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.Getter;
//
//@Getter
//@JsonIgnoreProperties(ignoreUnknown = true)
//public class KakaoUserInfo {
//    @JsonProperty("kakao_account")
//    private KakaoAccount kakaoAccount;
//
//    @Getter
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    static class KakaoAccount {
//        private String email;
//    }
//
//    public String getEmail() {
//        return this.kakaoAccount.email;
//    }
//}