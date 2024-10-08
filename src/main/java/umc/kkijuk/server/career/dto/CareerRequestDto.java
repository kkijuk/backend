package umc.kkijuk.server.career.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import umc.kkijuk.server.career.validation.ValidPeriod;

import java.time.LocalDate;

public class CareerRequestDto {
    @Getter
    @Builder
    @ValidPeriod
    public static class CreateCareerDto{

        @NotBlank(message = "활동명은 필수 입력 항목입니다. 최대 20자 까지 입력 가능")
        @Size(max = 20)
        @Schema(description = "활동명", example = "IT 서비스 개발 동아리", type="string")
        String careerName;

        @NotBlank(message = "활동 별칭은 필수 입력 항목입니다. 최대 20자 까지 입력 가능")
        @Size(max = 20)
        @Schema(description = "활동 별칭", example = "동아리", type="string")
        String alias;

        @Size(max = 50)
        @Schema(description = "활동 내역", example = "주요 활동 내용을 요약하여 작성해주세요. 최대 50자 까지 입력 가능 선택사항입니다.", type = "string")
        String summary;

        @NotNull(message = "활동 기간을 알고 있는지 여부를 나타냅니다.")
        @Schema(description = "활동 기간 인지 여부", example = "false", type = "boolean")
        Boolean isUnknown;


        @NotNull(message = "활동 시작 날짜는 필수 입력 항목입니다.")
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        @Schema(description = "활동 시작 날짜", example = "2024-04-14", type="string")
        LocalDate startDate;


        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        @Schema(description = "활동 종료 날짜", example = "2024-07-20", type = "string")
        LocalDate endDate;

        @NotNull(message = "활동 카테고리는 필수 선택 항목입니다.")
        @Schema(description = "활동 카테고리", example = "1", type = "int")
        Integer category;
    }

    @Getter
    @Builder
    public static class UpdateCareerDto{
        @Size(max = 20)
        @Nullable
        @Schema(description = "수정된 활동명", example = "수정된 활동명", type="string")
        String careerName;
        @Size(max = 20)
        @Nullable
        @Schema(description = "수정된 활동 별칭", example = "수정된 활동 별칭", type="string")
        String alias;
        @Size(max = 50)
        @Nullable
        @Schema(description = "수정된 활동 내역", example = "수정된 활동 내역",type = "string")
        String summary;
        @Nullable
        @Schema(description = "수정된 활동 기간 인지 여부", example = "false", type = "boolean")
        Boolean isUnknown;
        @Nullable
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        @Schema(description = "수정된 활동 시작 날짜", example = "2024-04-14", type="string")
        LocalDate startDate;

        @Nullable
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        @Schema(description = "수정된 활동 종료 날짜", example = "2024-07-20", type = "string")
        LocalDate endDate;

        @Nullable
        @Schema(description = "수정된 활동 카테고리", example = "1", type = "int")
        Integer category;
    }

    @Getter
    @Builder
    public static class SearchCareerDto {
        @Nullable
        @Schema(description = "검색어", example = "앱서비스", type="string")
        String search;

        @NotNull(message = "검색 조건에서 활동명을 선택하였는지를 나타냅니다. true 나 false 값을 주세요")
        @Schema(description = "활동명 조건 선택 여부", example = "true", type="Boolean")
        Boolean careerName;

        @NotNull(message = "검책 조건에서 활동 기록을 선택하였는지를 나타냅니다. true 나 false 값을 주세요.")
        @Schema(description = "활동 기록 조건 선택 여부", example = "false", type="Boolean")
        Boolean careerDetail;

        @NotNull(message = "활동 기간을 알고 있는지 여부를 나타냅니다.")
        @Schema(description = "태그 조건 선택 여부", example = "false", type="Boolean")
        Boolean tag;

        @Nullable
        @Schema(description = "정렬 기준", example = "desc", type="String")
        String sort;

        @Nullable
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        @Schema(description = "검색 기간 ( 시작 기간 )", example = "2024-04-14", type="string")
        LocalDate startDate;

        @Nullable
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        @Schema(description = "검색 기간 ( 종료 기간 )", example = "2024-07-20", type = "string")
        LocalDate endDate;

        public void setCareerName(Boolean careerName) {
            this.careerName = careerName;
        }
        public void setCareerDetail(Boolean careerDetail) {
            this.careerDetail = careerDetail;
        }
    }


}
