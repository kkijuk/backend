package umc.kkijuk.server.record.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.kkijuk.server.common.domian.base.BaseEntity;

import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Award extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "award_id")
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Size(max = 30)
    private String competitionName;
    @Size(max = 15)
    private String administer;
    @Size(max = 15)
    private String awardName;
    private LocalDate acquireDate;

    public void changeAwardInfo(String competitionName, String administer, String awardName, LocalDate acquireDate) {
        this.competitionName = competitionName;
        this.administer = administer;
        this.awardName = awardName;
        this.acquireDate = acquireDate;
    }

}
