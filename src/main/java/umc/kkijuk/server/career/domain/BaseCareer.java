package umc.kkijuk.server.career.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;



@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseCareer {

    @Column(nullable = false)
    private Long memberId;
    @Column(length = 20)
    private String name;
    @Column(length = 20)
    private String alias;
    private Boolean unknown;
    @Column(length = 500)
    private String summary;
    private int year;
    private LocalDate startdate;
    private LocalDate enddate;

    @CreatedDate
    @Column(length = 6)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(length = 6)
    private LocalDateTime updatedAt;

    public BaseCareer(Long memberId, String name, String alias, Boolean unknown,
                      LocalDate startdate, LocalDate enddate) {
        this.memberId = memberId;
        this.name = name;
        this.alias = alias;
        this.unknown = unknown;
        this.startdate = startdate;
        this.enddate = enddate;
    }

    public void updateBaseCareer(String name, String alias, Boolean unknown,
                                 LocalDate startdate, LocalDate enddate) {
        this.name = name;
        this.alias = alias;
        this.unknown = unknown;
        this.startdate = startdate;
        this.enddate = enddate;
    }
    public void setEnddate(LocalDate enddate) {
        this.enddate = enddate;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }

    public abstract Long getId();

}