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
public class Project extends BaseCareer{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int teamSize;
    private Boolean isTeam;
    private int contribution;
    @Enumerated(EnumType.STRING)
    private ProjectType location;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setSummary(String summary) {
        super.setSummary(summary);
    }

    @Builder
    public Project(Long memberId, String name, String alias, Boolean unknown,
                   LocalDate startdate, LocalDate enddate,
                   int teamSize, Boolean isTeam, int contribution, ProjectType location) {
        super(memberId, name, alias, unknown, startdate, enddate);
        this.teamSize = teamSize;
        this.isTeam = isTeam;
        this.contribution = contribution;
        this.location = location;
    }
    public void updateProject(String name, String alias, Boolean unknown,
                              LocalDate startdate, LocalDate enddate,
                              int teamSize, Boolean isTeam, int contribution, ProjectType location) {
        this.updateBaseCareer(name, alias, unknown, startdate, enddate);
        this.teamSize = teamSize;
        this.isTeam = isTeam;
        this.contribution = contribution;
        this.location = location;
    }
}
