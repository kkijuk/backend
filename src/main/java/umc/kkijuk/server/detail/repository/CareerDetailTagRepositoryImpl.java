package umc.kkijuk.server.detail.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import umc.kkijuk.server.detail.domain.mapping.CareerDetailTag;

@Repository
@RequiredArgsConstructor
public class CareerDetailTagRepositoryImpl implements CareerDetailTagRepository{
    private final CareerDetailTagJpaRepository careerDetailTagJpaRepository;
    @Override
    public void delete(CareerDetailTag careerDetailTag) {
        careerDetailTagJpaRepository.delete(careerDetailTag);
    }
}
