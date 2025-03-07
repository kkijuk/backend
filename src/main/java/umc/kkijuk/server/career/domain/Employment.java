package umc.kkijuk.server.career.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Employment extends BaseCareer{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private JobType type;
    private String position;
    private String field;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setSummary(String summary) {
        super.setSummary(summary);
    }

    @Builder
    public Employment(Long memberId, String name, String alias, Boolean unknown,
                      LocalDate startdate, LocalDate enddate,
                      JobType type, String position, String field) {
        super(memberId, name, alias, unknown, startdate, enddate);
        this.type = type;
        this.position = position;
        this.field = field;
    }

    public void updateEmployment(String name, String alias, Boolean unknown,
                                 LocalDate startdate, LocalDate enddate,
                                 JobType type,String position, String field) {
        this.updateBaseCareer(name, alias, unknown, startdate, enddate);
        this.type = type;
        this.position = position;
        this.field = field;
    }
}
