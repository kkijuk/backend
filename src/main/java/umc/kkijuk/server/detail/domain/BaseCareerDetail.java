package umc.kkijuk.server.detail.domain;

import jakarta.persistence.*;
import lombok.*;
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

    @Builder.Default
    @OneToMany(mappedBy = "baseCareerDetail", cascade = CascadeType.ALL)
    private List<CareerDetailTag> careerTagList = new ArrayList<>();



    public void updateBaseCareerDetail(String title, String content,
                                       LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
