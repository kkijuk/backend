package umc.kkijuk.server.record.controller.response;

import lombok.*;
import umc.kkijuk.server.record.domain.Education;

import java.time.LocalDate;
import java.time.YearMonth;

@Data
@AllArgsConstructor
@Getter @Setter
@Builder
public class EducationResponse {
    private Long educationId;
    private String category;
    private String schoolName;
    private String major;
    private String state;
    private LocalDate admissionDate;
    private LocalDate graduationDate;
    private Boolean isCurrent;

    public EducationResponse(Education education) {
        this.educationId = education.getId();
        this.category = education.getCategory();
        this.schoolName = education.getSchoolName();
        this.major = education.getMajor();
        this.state = education.getState();
        this.admissionDate = education.getAdmissionDate();
        this.graduationDate = education.getGraduationDate();
        this.isCurrent=determineIsCurrent(graduationDate);
    }

    private static Boolean determineIsCurrent(LocalDate graduationDate) {
        return graduationDate.isAfter(LocalDate.now());
    }
}
