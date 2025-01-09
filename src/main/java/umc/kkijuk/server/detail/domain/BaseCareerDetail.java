package umc.kkijuk.server.detail.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.kkijuk.server.career.domain.*;
import umc.kkijuk.server.common.domian.base.BaseEntity;
import umc.kkijuk.server.detail.domain.mapping.CareerDetailTag;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseCareerDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30)
    private String title;
    @Column(length = 800)
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private CareerType careerType;

    @Column(nullable = false)
    private Long careerId;


    @Column(nullable = false)
    private Long memberId;

    @OneToMany(mappedBy = "baseCareerDetail", cascade = CascadeType.ALL)
    private List<CareerDetailTag> careerTagList = new ArrayList<>();




//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="activity_id")
//    private Activity activity;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="circle_id")
//    private Circle circle;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="competition_id")
//    private Competition competition;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="edu_id")
//    private EduCareer eduCareer;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="employment_id")
//    private Employment employment;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="project_id")
//    private Project project;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="etc_id")
//    private CareerEtc etc;




    public void updateBaseCareerDetail(String title, String content,
                                       LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
