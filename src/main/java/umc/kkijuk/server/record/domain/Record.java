package umc.kkijuk.server.record.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.kkijuk.server.common.domian.base.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name="record")
@Getter
@NoArgsConstructor
public class Record extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    private String address;
    private String profileImageUrl;


    @Builder
    public Record(Long memberId, String address, String profileImageUrl) {
        this.memberId = memberId;
        this.address = address;
        this.profileImageUrl = profileImageUrl;
    }

    public void update(String address, String profileImageUrl) {
        this.address = address;
        this.profileImageUrl = profileImageUrl;
    }

    public void updateTimestamp() {
        setUpdatedAt(LocalDateTime.now());
    }
}
