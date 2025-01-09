package umc.kkijuk.server.career.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.kkijuk.server.detail.domain.BaseCareerDetail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EduCareer extends BaseCareer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String organizer;
    private int time;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setSummary(String summary) {
        super.setSummary(summary);
    }

    @Builder
    public EduCareer(Long memberId, String name, String alias, Boolean unknown,
                     LocalDate startdate, LocalDate enddate,
                     String organizer, int time) {
        super(memberId, name, alias, unknown, startdate, enddate);
        this.organizer = organizer;
        this.time = time;
    }

    public void updateEduCareer(String name, String alias, Boolean unknown,
                                LocalDate startdate, LocalDate enddate,
                                String organizer, int time) {
        this.updateBaseCareer(name, alias, unknown, startdate, enddate);
        this.organizer = organizer;
        this.time = time;
    }
}
