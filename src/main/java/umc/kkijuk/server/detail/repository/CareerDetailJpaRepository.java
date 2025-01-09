package umc.kkijuk.server.detail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.kkijuk.server.detail.domain.BaseCareerDetail;
import umc.kkijuk.server.detail.domain.CareerType;

import java.util.List;

public interface CareerDetailJpaRepository extends JpaRepository<BaseCareerDetail,Long> {
//    @Query("SELECT DISTINCT bcd FROM BaseCareerDetail bcd " +
//            "LEFT JOIN FETCH bcd.careerTagList ct " +
//            "LEFT JOIN FETCH  ct.tag t " +
//            "WHERE bcd.competition = :competition")
//    List<BaseCareerDetail> findByCompetition(Competition competition);
//    @Query("SELECT DISTINCT bcd FROM BaseCareerDetail bcd " +
//            "LEFT JOIN FETCH bcd.careerTagList ct " +
//            "LEFT JOIN FETCH ct.tag t " +
//            "WHERE bcd.activity = :activity")
//    List<BaseCareerDetail> findByActivity(Activity activity);
//    @Query("SELECT DISTINCT bcd FROM BaseCareerDetail bcd " +
//            "LEFT JOIN FETCH bcd.careerTagList ct " +
//            "LEFT JOIN FETCH ct.tag t " +
//            "WHERE bcd.circle = :circle")
//    List<BaseCareerDetail> findByCircle(Circle circle);
//    @Query("SELECT DISTINCT bcd FROM BaseCareerDetail bcd " +
//            "LEFT JOIN FETCH bcd.careerTagList ct " +
//            "LEFT JOIN FETCH ct.tag t " +
//            "WHERE bcd.project = :project")
//    List<BaseCareerDetail> findByProject(Project project);
//    @Query("SELECT DISTINCT bcd FROM BaseCareerDetail bcd " +
//            "LEFT JOIN FETCH bcd.careerTagList ct " +
//            "LEFT JOIN FETCH ct.tag t " +
//            "WHERE bcd.eduCareer = :edu")
//    List<BaseCareerDetail> findByEduCareer(EduCareer edu);
//    @Query("SELECT DISTINCT bcd FROM BaseCareerDetail bcd " +
//            "LEFT JOIN FETCH bcd.careerTagList ct " +
//            "LEFT JOIN FETCH ct.tag t " +
//            "WHERE bcd.employment = :emp")
//    List<BaseCareerDetail> findByEmployment(Employment emp);
//
//    @Query("SELECT DISTINCT bcd FROM BaseCareerDetail bcd " +
//            "LEFT JOIN FETCH bcd.careerTagList ct " +
//            "LEFT JOIN FETCH ct.tag t " +
//            "WHERE bcd.etc = :etc")
//    List<BaseCareerDetail> findByEtc(CareerEtc etc);

    @Query("SELECT DISTINCT bcd FROM BaseCareerDetail  bcd " +
            "LEFT JOIN FETCH bcd.careerTagList ct " +
            "LEFT JOIN FETCH ct.tag t "+
            "WHERE bcd.careerType = :careerType AND bcd.careerId = :careerId"
    )
    List<BaseCareerDetail> findByCareerIdAndCareerType(@Param("careerType") CareerType careerType,
                                                       @Param("careerId") Long careerId);
    @Query("SELECT DISTINCT bcd FROM BaseCareerDetail bcd " +
            "LEFT JOIN FETCH bcd.careerTagList ct " +
            "LEFT JOIN FETCH ct.tag t " +
            "WHERE bcd.memberId = :memberId " +
            "AND(bcd.title LIKE %:keyword% OR bcd.content LIKE %:keyword%)")
    List<BaseCareerDetail> findByMemberIdAndKeyword(@Param("memberId") Long memberId, @Param("keyword") String keyword);

    @Query("SELECT DISTINCT bcd FROM BaseCareerDetail bcd "+
            "JOIN FETCH bcd.careerTagList ct " +
            "JOIN FETCH ct.tag t " +
            "WHERE EXISTS (SELECT 1 FROM CareerDetailTag ct2 WHERE ct2.baseCareerDetail=bcd AND ct2.tag.id=:tagId)" )
    List<BaseCareerDetail> findByTag(@Param("tagId") Long tagId);
}
