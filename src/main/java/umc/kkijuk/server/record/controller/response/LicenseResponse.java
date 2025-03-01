package umc.kkijuk.server.record.controller.response;

import lombok.*;
import umc.kkijuk.server.record.domain.License;
import umc.kkijuk.server.record.domain.LicenseTag;

import java.time.LocalDate;
import java.time.YearMonth;

@Data
@AllArgsConstructor
@Getter
@Builder
public class LicenseResponse {
    private Long id;
    private LicenseTag licenseTag;
    private String licenseName;
    private String administer;
    private String licenseNumber;
    private String licenseGrade;
    private LocalDate acquireDate;

    public LicenseResponse(License license) {
        this.id = license.getId();
        this.licenseTag = license.getLicenseTag();
        this.licenseName = license.getLicenseName();
        this.administer = license.getAdminister();
        this.licenseNumber = license.getLicenseNumber();
        this.licenseGrade = license.getLicenseGrade();
        this.acquireDate = license.getAcquireDate();
    }
}
