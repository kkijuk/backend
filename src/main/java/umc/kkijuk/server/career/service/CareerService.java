package umc.kkijuk.server.career.service;

import umc.kkijuk.server.career.domain.Career;
import umc.kkijuk.server.career.dto.CareerRequestDto;

import java.util.List;
import java.util.Optional;

public interface CareerService {
    Career createCareer(CareerRequestDto.CareerDto request);
    void deleteCareer(Long careerId);
    Optional<Career> findCareer(Long value);
    Career updateCareer(Long careerId, CareerRequestDto.UpdateCareerDto request);

}
