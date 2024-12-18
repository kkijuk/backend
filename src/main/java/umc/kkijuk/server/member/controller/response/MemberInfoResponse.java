package umc.kkijuk.server.member.controller.response;

import lombok.Builder;
import lombok.Data;
import umc.kkijuk.server.member.domain.Role;

import java.time.LocalDate;

@Data
@Builder
public class MemberInfoResponse {
    private Long socialId;
    private String email;
    private String name;
    private String phoneNumber;
    private LocalDate birthDate;
    private Role role;
}
