package umc.kkijuk.server.member.controller.response;

import lombok.Builder;
import lombok.Data;
import umc.kkijuk.server.member.domain.SocialType;

import java.time.LocalDate;

@Data
public class MemberEmailResponse {
    private String email;
    private SocialType socialType;

    public MemberEmailResponse(){
    }

    @Builder
    public MemberEmailResponse(String email, SocialType socialType){
        this.email = email;
        this.socialType = socialType;
    }
}
