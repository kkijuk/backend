package umc.kkijuk.server.career.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CareerEtc extends BaseCareer{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Override
    public Long getId() {
        return id;
    }
    @Override
    public void setSummary(String summary) {
        super.setSummary(summary);
    }
    @Builder
    public CareerEtc(Long memberId, String name, String alias, Boolean unknown,
                    LocalDate startdate, LocalDate enddate) {
        super(memberId, name, alias, unknown,startdate, enddate);
    }
    public void updateCareerEtc(String name, String alias, Boolean unknown,
                                 LocalDate startdate, LocalDate enddate ) {
        this.updateBaseCareer(name, alias, unknown, startdate, enddate);
    }

}
