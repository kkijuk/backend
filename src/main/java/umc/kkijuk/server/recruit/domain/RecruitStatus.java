package umc.kkijuk.server.recruit.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum RecruitStatus {
    UNAPPLIED, PLANNED, APPLYING, REJECTED, ACCEPTED;

    @JsonCreator
    public static RecruitStatus parsing(String value) {
        return Stream.of(RecruitStatus.values())
                .filter(recruitStatus -> recruitStatus.toString().equals(value.toUpperCase()))
                .findFirst()
                .orElse(null);
    }
}
