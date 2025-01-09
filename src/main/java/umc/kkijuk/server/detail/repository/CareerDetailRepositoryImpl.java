package umc.kkijuk.server.detail.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import umc.kkijuk.server.detail.domain.BaseCareerDetail;
import umc.kkijuk.server.detail.domain.CareerType;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CareerDetailRepositoryImpl implements CareerDetailRepository{
    private final CareerDetailJpaRepository careerDetailJpaRepository;
    @Override
    public BaseCareerDetail save(BaseCareerDetail baseCareerDetail) {
        return careerDetailJpaRepository.save(baseCareerDetail);
    }

    @Override
    public Optional<BaseCareerDetail> findById(Long detailId) {
        return careerDetailJpaRepository.findById(detailId);
    }

    @Override
    public void delete(BaseCareerDetail baseCareerDetail) {
        careerDetailJpaRepository.delete(baseCareerDetail);
    }

    @Override
    public List<BaseCareerDetail> findByCareerIdAndCareerType(CareerType careerType, Long id) {
        return careerDetailJpaRepository.findByCareerIdAndCareerType(careerType,id);
    }

    @Override
    public List<BaseCareerDetail> findByMemberIdAndKeyword(Long id, String keyword) {
        return careerDetailJpaRepository.findByMemberIdAndKeyword(id, keyword);
    }

    @Override
    public List<BaseCareerDetail> findByTag(Long id) {
        return careerDetailJpaRepository.findByTag(id);
    }
}
