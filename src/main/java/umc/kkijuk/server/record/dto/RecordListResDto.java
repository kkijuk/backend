package umc.kkijuk.server.record.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.kkijuk.server.career.domain.Career;

import java.time.LocalDate;
import java.time.Period;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecordListResDto {
    private Long careerId;
    private String careerName;
    private String alias;
    private String category;
    private LocalDate startDate;
    private LocalDate endDate;
    private int period;
    private String summary;
    private Boolean isCurrent;

    @Builder
    public RecordListResDto(Career career) {
        this.careerId = career.getId();
        this.careerName=career.getName();
        this.alias=career.getAlias();
        this.category=career.getCategory().getName();
        this.startDate=career.getStartdate();
        this.endDate = career.getEnddate();
        this.period=calculatePeriodInMonths(startDate, endDate);
        this.summary=career.getSummary();
        this.isCurrent=determineIsCurrent(endDate);
    }

    private static int calculatePeriodInMonths(LocalDate startDate, LocalDate endDate) {
        return Period.between(startDate, endDate).getMonths();
    }

    private static Boolean determineIsCurrent(LocalDate endDate) {
        return endDate.isAfter(LocalDate.now());
    }

}