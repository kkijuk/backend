package umc.kkijuk.server.career.dto.converter;

import umc.kkijuk.server.career.domain.Career;
import umc.kkijuk.server.career.dto.CareerRequestDto;
import umc.kkijuk.server.career.dto.CareerResponseDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CareerConverter {

    public static Career toCareer(CareerRequestDto.CreateCareerDto request){
        return Career.builder()
                .name(request.getCareerName())
                .alias(request.getAlias())
                .summary(request.getSummary())
                .startdate(request.getStartDate())
                .enddate(request.getEndDate())
                .current(request.getIsCurrent())
                .enddate(request.getEndDate())
                .build();
    }
    public static CareerResponseDto.CareerResultDto toCareerResultDto(Career career){
        return CareerResponseDto.CareerResultDto.builder()
                .careerId(career.getId())
                .build();
    }
    public static CareerResponseDto.CareerDto toCareerDto(Career career) {
        return CareerResponseDto.CareerDto.builder()
                .id(career.getId())
                .careerName(career.getName())
                .alias(career.getAlias())
                .summary(career.getSummary())
                .isCurrent(career.getCurrent())
                .startDate(career.getStartdate())
                .endDate(career.getEnddate())
                .year(career.getYear())
                .categoryId(Math.toIntExact(career.getCategory().getId()))
                .categoryName(career.getCategory().getName())
                .build();
    }

    public static List<CareerResponseDto.CareerGroupedByCategoryDto> toCareerGroupedByCategoryDto( Map<String, List<Career>> groupedCareers ) {
        return groupedCareers.entrySet().stream()
                .map(entry -> CareerResponseDto.CareerGroupedByCategoryDto.builder()
                        .categoryName(entry.getKey())
                        .count(entry.getValue().size())
                        .careers(entry.getValue().stream()
                                .map(career -> CareerResponseDto.CareerDto.builder()
                                        .id(career.getId())
                                        .careerName(career.getName())
                                        .alias(career.getAlias())
                                        .summary(career.getSummary())
                                        .year(career.getYear())
                                        .startDate(career.getStartdate())
                                        .endDate(career.getEnddate())
                                        .isCurrent(career.getCurrent())
                                        .categoryId(Math.toIntExact(career.getCategory().getId()))
                                        .categoryName(career.getCategory().getName())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    public static List<CareerResponseDto.CareerGroupedByYearDto> toCareerGroupedByYearDto(Map<String, List<Career>> groupedCareers) {
        return groupedCareers.entrySet().stream()
                .map(entry -> CareerResponseDto.CareerGroupedByYearDto.builder()
                        .year(Integer.parseInt(entry.getKey()))
                        .count(entry.getValue().size())
                        .careers(entry.getValue().stream()
                                .map(career -> CareerResponseDto.CareerDto.builder()
                                        .id(career.getId())
                                        .careerName(career.getName())
                                        .alias(career.getAlias())
                                        .summary(career.getSummary())
                                        .year(career.getYear())
                                        .startDate(career.getStartdate())
                                        .endDate(career.getEnddate())
                                        .isCurrent(career.getCurrent())
                                        .categoryId(Math.toIntExact(career.getCategory().getId()))
                                        .categoryName(career.getCategory().getName())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }


}
