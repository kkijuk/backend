package umc.kkijuk.server.career.controller.response;

import java.time.LocalDate;

public interface BaseCareerResponse {
    LocalDate getEndDate();

    LocalDate getStartDate();
}
