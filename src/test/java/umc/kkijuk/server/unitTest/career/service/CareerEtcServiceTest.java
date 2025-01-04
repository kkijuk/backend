package umc.kkijuk.server.unitTest.career.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import umc.kkijuk.server.career.controller.response.EtcResponse;
import umc.kkijuk.server.career.domain.CareerEtc;
import umc.kkijuk.server.career.dto.EtcReqDto;
import umc.kkijuk.server.career.repository.CareerEtcRepository;
import umc.kkijuk.server.career.service.CareerService;
import umc.kkijuk.server.career.service.CareerServiceImpl;
import umc.kkijuk.server.common.domian.exception.OwnerMismatchException;
import umc.kkijuk.server.common.domian.exception.ResourceNotFoundException;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.domain.State;
import umc.kkijuk.server.unitTest.mock.FakeCareerEtcRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CareerEtcServiceTest {
    private CareerService careerService;
    private final Long testMemberId = 3333L;
    private Member requestMember;
    private final LocalDate testStartDate = LocalDate.of(2023, 7, 19);
    private final LocalDate testEndDate = LocalDate.of(2023, 12, 19);
    //fake repository
    private CareerEtcRepository etcRepository;
    @BeforeEach
    void init() {
        this.requestMember = Member.builder()
                .id(testMemberId)
                .email("test-email@test.com")
                .name("test-name")
                .phoneNumber("test-test-test")
                .birthDate(LocalDate.of(2024, 7, 25))
                .password("test-password")
                .userState(State.ACTIVATE)
                .build();

        this.etcRepository = new FakeCareerEtcRepository();
        this.careerService = CareerServiceImpl.builder()
                .etcRepository(etcRepository)
                .build();

        CareerEtc etc1 = CareerEtc.builder()
                .memberId(testMemberId)
                .name("test activity")
                .alias("test alias")
                .unknown(false)
                .startdate(testStartDate)
                .enddate(testEndDate)
                .build();

        CareerEtc etc2 = CareerEtc.builder()
                .memberId(testMemberId)
                .name("test activity")
                .alias("test alias")
                .unknown(false)
                .startdate(testStartDate)
                .enddate(testEndDate)
                .build();

        etcRepository.save(etc1);
        etcRepository.save(etc2);
    }
    @Test
    @DisplayName("[create] 새로운 CareerEtc 만들기 - 정상 요청")
    void testCreateCareerEtc() {
        //given
        EtcReqDto etcReqDto = EtcReqDto.builder()
                .name("기타 제목")
                .alias("기타 별칭")
                .unknown(false)
                .startdate(LocalDate.of(2023,5,1))
                .enddate(LocalDate.of(2023,12,12))
                .build();
        //when
        EtcResponse response = careerService.createEtc(requestMember,etcReqDto);
        //then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(3L),
                () -> assertThat(response.getName()).isEqualTo("기타 제목"),
                () -> assertThat(response.getAlias()).isEqualTo("기타 별칭"),
                () -> assertThat(response.getStartdate()).isEqualTo(LocalDate.of(2023,5,1)),
                () -> assertThat(response.getEndDate()).isEqualTo(LocalDate.of(2023,12,12)),
                () -> assertThat(response.getUnknown()).isEqualTo(false)
        );
    }
    @Test
    @DisplayName("[create] 새로운 CareerEtc 만들기 - unknown값이 true일 경우 endDate가 현재 날짜로 설정")
    void testCreateCareerEtcyWithInvalidFields(){
        //given
        EtcReqDto etcReqDto = EtcReqDto.builder()
                .name("기타 제목")
                .alias("기타 별칭")
                .unknown(true)
                .startdate(LocalDate.of(2023,5,1))
                .build();
        //when
        EtcResponse response = careerService.createEtc(requestMember,etcReqDto);

        //then
        assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.getEndDate()).isEqualTo(LocalDate.now())
        );
    }

    @Test
    @DisplayName("[update] 기존에 존재하는 CareerEtc 수정하기 - unknown값이 true일 경우 endDate 값이 현재 날짜로 설정")
    void testUpdateCareerEtcWithUnknown() {
        //given
        EtcReqDto etcReqDto = EtcReqDto.builder()
                .name("수정된 기타 제목")
                .alias("수정된 기타 별칭")
                .unknown(true)
                .startdate(LocalDate.of(2023,5,1))
                .build();
        //when

        //when
        EtcResponse updatedResponse = careerService.updateEtc(requestMember, 1L, etcReqDto);
        //then
        assertAll(
                () -> assertThat(updatedResponse.getId()).isEqualTo(1L),
                () -> assertThat(updatedResponse.getName()).isEqualTo("수정된 기타 제목"),
                () -> assertThat(updatedResponse.getAlias()).isEqualTo("수정된 기타 별칭"),
                () -> assertThat(updatedResponse.getStartdate()).isEqualTo(LocalDate.of(2023,5,1)),
                () -> assertThat(updatedResponse.getEndDate()).isEqualTo(LocalDate.now()),
                () -> assertThat(updatedResponse.getUnknown()).isEqualTo(true)
        );

    }

    @Test
    @DisplayName("[update] 기존에 존재하는 CareerEtc 수정하기 - 정상 요청")
    void testUpdateCareerEtc() {
        //given
        EtcReqDto etcReqDto = EtcReqDto.builder()
                .name("수정된 기타 제목")
                .alias("수정된 기타 별칭")
                .unknown(false)
                .startdate(LocalDate.of(2023,5,1))
                .enddate(LocalDate.of(2023,7,1))
                .build();
        //when

        //when
        EtcResponse updatedResponse = careerService.updateEtc(requestMember, 1L, etcReqDto);
        //then
        assertAll(
                () -> assertThat(updatedResponse.getId()).isEqualTo(1L),
                () -> assertThat(updatedResponse.getName()).isEqualTo("수정된 기타 제목"),
                () -> assertThat(updatedResponse.getAlias()).isEqualTo("수정된 기타 별칭"),
                () -> assertThat(updatedResponse.getStartdate()).isEqualTo(LocalDate.of(2023,5,1)),
                () -> assertThat(updatedResponse.getEndDate()).isEqualTo(LocalDate.of(2023,7,1)),
                () -> assertThat(updatedResponse.getUnknown()).isEqualTo(false)
        );

    }


    @Test
    @DisplayName("[update] 기존에 존재하는 CareerEtc 수정하기 - 없는 리소스로의 요청일 경우 ResourceNotFoundException 발생")
    void testUpdateCareerEtcWithResourceNotFoundException() {
        //given
        EtcReqDto etcReqDto = EtcReqDto.builder()
                .name("수정된 기타 제목")
                .alias("수정된 기타 별칭")
                .unknown(true)
                .startdate(LocalDate.of(2023,5,1))
                .enddate(LocalDate.of(2023,7,1))
                .build();

        //when
        //then
        assertThatThrownBy(() -> careerService.updateEtc(requestMember, 999L, etcReqDto)).isInstanceOf(ResourceNotFoundException.class);
    }
    @Test
    @DisplayName("[update] 기존에 존재하는 CareerEtc 수정하기 - 다른 사용자의 요청일 경우 OwnerMismatchException 발생")
    void testUpdateCareerEtcWithOwnerMismatchException() {
        //given
        EtcReqDto etcReqDto = EtcReqDto.builder()
                .name("수정된 기타 제목")
                .alias("수정된 기타 별칭")
                .unknown(true)
                .startdate(LocalDate.of(2023,5,1))
                .enddate(LocalDate.of(2023,7,1))
                .build();

        Member anotherMember = Member.builder().id(999L).build();
        //when
        //then
        assertThatThrownBy(() -> careerService.updateEtc(anotherMember, 1L, etcReqDto))
                .isInstanceOf(OwnerMismatchException.class);

    }

    @Test
    @DisplayName("[delete] 기존에 존재하는 CareerEtc 제거하기 - 정상 요청")
    void testDeleteCareerEtc() {
        //given
        Long etcId = 1L;
        //when
        careerService.deleteBaseCareer(requestMember,etcId,"etc");
        //then
        assertThat(etcRepository.findById(etcId)).isEmpty();

    }
    @Test
    @DisplayName("[delete] 기존에 존재하는 CareerEtc 제거하기 - 없는 타입으로의 요청일 경우 IllegalArgumentException 발생")
    void testDeleteCareerEtcWithIllegalArgumentException() {
        //given
        Long etcId = 1L;
        //when
        //then
        assertThatThrownBy(() -> careerService.deleteBaseCareer(requestMember, etcId, "noneType"))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    @DisplayName("[delete] 기존에 존재하는 CareerEtc 제거하기 - 없는 리소스로의 요청일 경우 ResourceNotFoundException 발생")
    void testDeleteCareerEtcWithResourceNotFoundException() {
        //given
        Long etcId = 9999L;
        //when
        //then
        assertThatThrownBy(() -> careerService.deleteBaseCareer(requestMember, etcId, "etc"))
                .isInstanceOf(ResourceNotFoundException.class);

    }
    @Test
    @DisplayName("[delete] 기존에 존재하는 CareerEtc 제거하기 - 다른 사용자의 요청일 경우 OwnerMismatchException 발생")
    void testDeleteCareerEtcWithOwnerMismatchException() {
        //given
        Long etcId = 1L;
        Member anotherMember = Member.builder().id(999L).build();
        //when
        //then
        assertThatThrownBy(() -> careerService.deleteBaseCareer(anotherMember, etcId, "etc"))
                .isInstanceOf(OwnerMismatchException.class);
    }

}
