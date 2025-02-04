package umc.kkijuk.server.introduce.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.kkijuk.server.common.domian.base.BaseEntity;
import umc.kkijuk.server.recruit.infrastructure.RecruitEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "master_introduce")
@Getter
@NoArgsConstructor
public class MasterIntroduce extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    private String oneLiner;

    @NotNull
    @OneToMany(mappedBy = "masterIntroduce", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MasterQuestion> masterQuestion;

    @NotNull
    private int state;

    @Builder
    public MasterIntroduce(Long memberId, String oneLiner,List<MasterQuestion> masterQuestion, int state) {
        this.memberId = memberId;
        this.oneLiner = oneLiner;
        this.masterQuestion = masterQuestion;
        this.state = state;
        setMasterQuestions(oneLiner, masterQuestion);
    }

    public void setMasterQuestions(String oneLiner,List<MasterQuestion> masterQuestions) {
        this.masterQuestion = masterQuestions;
        this.oneLiner = oneLiner;
        for (MasterQuestion masterQuestion : masterQuestions) {
            masterQuestion.setMasterIntroduce(this);
        }
    }

    public void setState(int state) {
        this.state=state;
    }

    public void setOneLiner(String oneLiner){
        this.oneLiner = oneLiner;
    }


}
