package umc.kkijuk.server.introduce.controller.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class FindIntroduceResponse implements SearchResultResponse {
    private Long introId;
    private String title;
    private String content;
    private int state;
    private LocalDate updatedDate;

//    @Override
//    public LocalDate getCreatedDate() {
//        return createdDate;
//    }

    @Override
    public LocalDate getUpdatedDate() {
        return updatedDate;
    }
}
