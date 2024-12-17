package umc.kkijuk.server.member.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class MemberRegisterDto {
    private Long kakaoId;
    private String email;
    private String name;
    private String phoneNumber;
    private LocalDate birthDate;
}
