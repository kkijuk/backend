package umc.kkijuk.server.record.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.kkijuk.server.common.domian.base.BaseEntity;

import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="license")
public class License extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @NotNull
    private LicenseTag licenseTag;

    @NotNull
    @Size(max = 30)
    private String licenseName;
    @Size(max = 15)
    private String administer;
    @Size(max = 30)
    private String licenseNumber;
    @Size(max = 10)
    private String licenseGrade;
    private LocalDate acquireDate;


    public void changeLicenseInfo(LicenseTag licenseTag, String licenseName, String administer, String licenseNumber, String licenseGrade, LocalDate acquireDate){
        this.licenseTag = licenseTag;
        this.licenseName = licenseName;
        this.administer = administer;
        this.licenseNumber = licenseNumber;
        this.licenseGrade = licenseGrade;
        this.acquireDate = acquireDate;
    }


}
