package umc.kkijuk.server.introduce.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.kkijuk.server.common.domian.base.BaseEntity;
import umc.kkijuk.server.recruit.infrastructure.RecruitEntity;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="introduce")
@Getter
@NoArgsConstructor
public class Introduce extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "recruit_id", nullable = false)
    @NotNull
    private RecruitEntity recruit;

    @Column(nullable = false)
    private Long memberId;

    @NotNull
    @OneToMany(mappedBy = "introduce", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

    @NotNull
    private int state;

    @Builder
    public Introduce(Long memberId, RecruitEntity recruit, List<Question> questions, int state) {
        this.memberId = memberId;
        this.recruit = recruit;
        this.questions = questions;
        this.state = state;
        setQuestions(questions);
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
        for (Question question : questions) {
            question.setIntroduce(this);
        }
    }

    public void update(int state) {
        this.state=state;
    }

    public void updateTimestamp() {
        setUpdatedAt(LocalDateTime.now());
    }

}
