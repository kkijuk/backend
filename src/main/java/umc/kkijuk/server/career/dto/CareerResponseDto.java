package umc.kkijuk.server.career.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import umc.kkijuk.server.careerdetail.dto.CareerDetailResponseDto;

import java.time.LocalDate;
import java.util.List;
public class CareerResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CareerResultDto{
        private Long careerId;
    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CareerDto{
        Long id;
        String careerName;
        String alias;
        String summary;
        Boolean isUnknown;
        LocalDate startDate;
        LocalDate endDate;
        int year;
        String categoryName;
        int categoryId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CareerGroupedByCategoryDto {
        @Schema(description = "카테고리명",example = "동아리",type = "String")
        private String categoryName;
        @Schema(description = "카테고리 내 활동 개수 ( 예 : 카테고리가 동아리인 활동의 개수 )",example = "2",type="int")
        private int count;
        @Schema(description = "해당 카테고리 내 활동 목록")
        private List<CareerDto> careers;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CareerGroupedByYearDto{
        @Schema(description = "연도", example = "2024", type = "int")
        private int year;
        @Schema(description = "연도 내 활동 개수 ( 예 : 2024년에 진행된 활동의 개수 )", example = "2", type = "int")
        private int count;
        @Schema(description = "해당 연도 내 활동 목록")
        private List<CareerDto> careers;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CareerDetailDto{
        private Long id;
        private Long memberId;
        private String careerName;
        private String alias;
        private String summary;

        private Boolean isUnknown;
        private LocalDate startDate;
        private LocalDate endDate;
        private int year;

        private String categoryName;
        private int categoryId;

        private int totalDetailCount;
        private List<CareerDetailResponseDto.CareerDetailResult> details;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CareerNameSearchDto{
        private Long id;
        private String careerName;
        private String alias;
        private String summary;
        private LocalDate startDate;
        private LocalDate endDate;
        String categoryName;
        private CareerDetailResponseDto.CareerDetailResult careerDetail;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CareerSearchDto{
        Long id;
        String careerName;
        String alias;
        String summary;
        LocalDate startDate;
        LocalDate endDate;
        String categoryName;
        List<CareerDetailResponseDto.CareerDetailResult> details;


    }




}
