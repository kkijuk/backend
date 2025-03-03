package umc.kkijuk.server.record.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.kkijuk.server.common.converter.YearMonthAttributeConverter;
import umc.kkijuk.server.common.domian.base.BaseEntity;

import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Table(name="education")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Education extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    private String category;
    private String schoolName;
    private String major;
    private String state;
    @Convert(converter = YearMonthAttributeConverter.class)
    private YearMonth admissionDate;
    @Convert(converter = YearMonthAttributeConverter.class)
    private YearMonth graduationDate;


    public void changeEducationInfo(String category, String schoolName, String major
            , String state, YearMonth admissionDate, YearMonth graduationDate) {
        this.category = category;
        this.schoolName = schoolName;
        this.major = major;
        this.state = state;
        this.admissionDate = admissionDate;
        this.graduationDate = graduationDate;
    }
}


