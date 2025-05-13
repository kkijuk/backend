package umc.kkijuk.server.record.controller.response;

import lombok.*;
import umc.kkijuk.server.career.controller.response.BaseCareerResponse;
import umc.kkijuk.server.career.controller.response.EduCareerResponse;
import umc.kkijuk.server.career.controller.response.EmploymentResponse;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.record.domain.Record;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class RecordResponse {
    private Long memberId;
    private Long recordId;
    private String address;
    private String profileImageUrl;
    private String updatedAt;
    private List<EducationResponse> educationList;
    private List<BaseCareerResponse> activitiesAndExperiences;
    private List<EmploymentResponse> employments;
    private List<BaseCareerResponse> projects;
    private List<EduCareerResponse> eduCareers;
    private List<AwardResponse> awards;
    private List<LicenseResponse> licenses;
    private List<SkillResponse> skills;
    private List<FileResponse> files;
    private String name;
    private LocalDate birthday;
    private String phone;
    private String email;


    // 이력서 있을 때
    @Builder
    public RecordResponse(Record record, Member member, List<EducationResponse> educationList,
                          List<EmploymentResponse> employments, List<BaseCareerResponse> activitiesAndExperiences,
                          List<BaseCareerResponse> projects, List<EduCareerResponse> eduCareers,
                          List<AwardResponse> awards, List<LicenseResponse> licenses,
                          List<SkillResponse> skills, List<FileResponse> files) {
        this.memberId = member.getId();
        this.recordId=record.getId();
        this.address = record.getAddress();
        this.profileImageUrl=record.getProfileImageUrl();
        this.educationList=educationList;
        this.employments = employments;
        this.activitiesAndExperiences = activitiesAndExperiences;
        this.projects = projects;
        this.eduCareers = eduCareers;
        this.awards = awards;
        this.licenses = licenses;
        this.skills = skills;
        this.files = files;
        this.name = member.getName();
        this.birthday=member.getBirthDate();
        this.phone=member.getPhoneNumber();
        this.email=member.getEmail();
        this.updatedAt = formatUpdatedAt(record.getUpdatedAt());
    }


    private String formatUpdatedAt(LocalDateTime updatedAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return updatedAt != null ? updatedAt.format(formatter) : null;
    }
}
