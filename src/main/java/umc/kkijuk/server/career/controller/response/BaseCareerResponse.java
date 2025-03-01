package umc.kkijuk.server.career.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;

public interface BaseCareerResponse {
    @JsonIgnore
    LocalDate getEndDate();

    @JsonIgnore
    LocalDate getStartDate();
}
