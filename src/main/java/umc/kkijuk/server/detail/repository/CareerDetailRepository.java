package umc.kkijuk.server.detail.repository;

import umc.kkijuk.server.detail.domain.BaseCareerDetail;
import umc.kkijuk.server.detail.domain.CareerType;

import java.util.List;
import java.util.Optional;

public interface CareerDetailRepository {
    BaseCareerDetail save(BaseCareerDetail newBaseCareerDetail);

    Optional<BaseCareerDetail> findById(Long detailId);

    void delete(BaseCareerDetail baseCareerDetail);

    List<BaseCareerDetail> findByCareerIdAndCareerType(CareerType careerType, Long id);

    List<BaseCareerDetail> findByMemberIdAndKeyword(Long id, String keyword);

    List<BaseCareerDetail> findByTag(Long id);
}
