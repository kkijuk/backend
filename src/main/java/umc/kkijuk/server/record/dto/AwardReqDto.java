package umc.kkijuk.server.record.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AwardReqDto {
    private String competitionName;
    private String administer;
    private String awardName;
    private LocalDate acquireDate;
}
