package umc.kkijuk.server.introduce.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.kkijuk.server.common.domian.exception.IntroFoundException;
import umc.kkijuk.server.common.domian.exception.IntroOwnerMismatchException;
import umc.kkijuk.server.common.domian.exception.ResourceNotFoundException;
import umc.kkijuk.server.introduce.controller.response.*;
import umc.kkijuk.server.introduce.domain.*;
import umc.kkijuk.server.introduce.dto.IntroduceReqDto;
import umc.kkijuk.server.introduce.dto.QuestionDto;
import umc.kkijuk.server.introduce.repository.IntroduceRepository;
import umc.kkijuk.server.introduce.repository.MasterIntroduceRepository;
import umc.kkijuk.server.introduce.repository.QuestionRepository;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.recruit.domain.Recruit;
import umc.kkijuk.server.recruit.infrastructure.RecruitEntity;
import umc.kkijuk.server.recruit.infrastructure.RecruitJpaRepository;
import umc.kkijuk.server.review.controller.port.ReviewService;
import umc.kkijuk.server.review.domain.Review;
import umc.kkijuk.server.review.domain.ReviewCreate;
import umc.kkijuk.server.review.service.port.ReviewRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class IntroduceServiceImpl implements IntroduceService {

    private final IntroduceRepository introduceRepository;
    private final RecruitJpaRepository recruitJpaRepository;
    private final QuestionRepository questionRepository;
    private final MasterIntroduceRepository masterIntroduceRepository;
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public IntroduceResponse saveIntro(Member requestMember, Long recruitId, IntroduceReqDto introduceReqDto) {
        RecruitEntity recruitEntity = recruitJpaRepository.findById(recruitId)
                .orElseThrow(() -> new ResourceNotFoundException("recruit ", recruitId));

        if (introduceRepository.findByRecruitId(recruitId).isPresent()) {
            throw new IntroFoundException("이미 자기소개서가 존재합니다");
        }

        List<Question> questions = introduceReqDto.getQuestionList().stream()
                .map(dto -> new Question(dto.getTitle(), dto.getContent(), dto.getNumber()))
                .collect(Collectors.toList());

        Introduce introduce = Introduce.builder()
                .memberId(requestMember.getId())
                .recruit(recruitEntity)
                .questions(questions)
                .state(introduceReqDto.getState())
                .build();

        introduceRepository.save(introduce);

        //작성 완료 처리 됐을 때 '서류'라는 이름의 공고 리뷰 자동 생성

        Recruit recruit = recruitEntity.toModel();

        if (introduceReqDto.getState() == 1) {
            // '서류' 제목의 리뷰가 이미 존재하는지 확인
            Optional<Review> existingReview = reviewRepository.findByRecruitAndTitle(recruit, "서류");
            if (existingReview.isEmpty()) {
                ReviewCreate reviewCreate = ReviewCreate.builder()
                        .title("서류")
                        .content("전형 후기가 없습니다.")
                        .date(LocalDate.now())
                        .build();
                reviewService.create(requestMember, recruit, reviewCreate);
            }
        }

        return new IntroduceResponse(introduce, introduceReqDto.getQuestionList());
    }

    @Override
    @Transactional
    public IntroduceResponse getIntro(Member requestMember, Long introId) {
        Introduce introduce = introduceRepository.findById(introId)
                .orElseThrow(() -> new ResourceNotFoundException("introduce ", introId));
        if (!introduce.getMemberId().equals(requestMember.getId())) {
            throw new IntroOwnerMismatchException();
        }

        List<QuestionDto> questionList = introduce.getQuestions()
                .stream()
                .map(question -> new QuestionDto(question.getTitle(), question.getContent(), question.getNumber()))
                .collect(Collectors.toList());

        return new IntroduceResponse(introduce, questionList);
    }

    @Override
    @Transactional
    public List<IntroduceListResponse> getIntroList(Member requestMember) {
        List<Introduce> introduces = introduceRepository.findAllByMemberId(requestMember.getId())
                .orElseThrow(IntroOwnerMismatchException::new);

        return introduces.stream()
                .map(IntroduceListResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public IntroduceResponse updateIntro(Member requestMember, Long introId, IntroduceReqDto introduceReqDto) throws Exception {
        Introduce introduce = introduceRepository.findById(introId)
                .orElseThrow(() -> new ResourceNotFoundException("introduce ", introId));

        if (!introduce.getMemberId().equals(requestMember.getId())) {
            throw new IntroOwnerMismatchException();
        }

        introduce.update(introduceReqDto.getState());

        List<Question> existingQuestions = introduce.getQuestions();
        Map<Integer, Question> existingQuestionsMap = existingQuestions.stream()
                .collect(Collectors.toMap(Question::getNumber, q -> q));

        List<QuestionDto> questionDtos = introduceReqDto.getQuestionList();
        List<Question> updatedQuestions = new ArrayList<>();

        for (QuestionDto questionDto : questionDtos) {
            Integer number = questionDto.getNumber();
            Question existingQuestion = existingQuestionsMap.get(number);

            if (existingQuestion != null) {
                existingQuestion.update(questionDto.getTitle(), questionDto.getContent());
                updatedQuestions.add(existingQuestion);
            } else {
                Question newQuestion = new Question();
                newQuestion.setTitle(questionDto.getTitle());
                newQuestion.setContent(questionDto.getContent());
                newQuestion.setNumber(questionDto.getNumber());
                newQuestion.setIntroduce(introduce);
                updatedQuestions.add(newQuestion);
            }
        }

        List<Question> toRemove = existingQuestions.stream()
                .filter(q -> !questionDtos.stream()
                        .anyMatch(dto -> dto.getNumber() == q.getNumber()))
                .collect(Collectors.toList());

        toRemove.forEach(question -> {
            introduce.getQuestions().remove(question);
            questionRepository.delete(question);
        });

        introduce.getQuestions().clear();
        introduce.getQuestions().addAll(updatedQuestions);

        introduceRepository.save(introduce);

        // "서류" 리뷰 자동 생성 로직 추가
        RecruitEntity recruitEntity = introduce.getRecruit(); // Introduce 엔티티에서 Recruit 가져오기
        Recruit recruit = recruitEntity.toModel();

        if (introduceReqDto.getState() == 1) {
            // "서류" 제목의 리뷰가 이미 존재하는지 확인
            Optional<Review> existingReview = reviewRepository.findByRecruitAndTitle(recruit, "서류");
            if (existingReview.isEmpty()) {
                ReviewCreate reviewCreate = ReviewCreate.builder()
                        .title("서류")
                        .content("전형 후기가 없습니다.")
                        .date(LocalDate.now())
                        .build();
                reviewService.create(requestMember, recruit, reviewCreate);
            }
        }

        List<QuestionDto> responseQuestionList = introduce.getQuestions().stream()
                .map(question -> QuestionDto.builder()
                        .title(question.getTitle())
                        .content(question.getContent())
                        .number(question.getNumber())
                        .build())
                .collect(Collectors.toList());


        return IntroduceResponse.builder()
                .introduce(introduce)
                .questionList(responseQuestionList)
                .build();
    }

    @Override
    @Transactional
    public Long deleteIntro(Member requestMember, Long introId) {
        Introduce introduce = introduceRepository.findById(introId)
                .orElseThrow(() -> new ResourceNotFoundException("introduce ", introId));
        if (!introduce.getMemberId().equals(requestMember.getId())) {
            throw new IntroOwnerMismatchException();
        }

        introduceRepository.delete(introduce);

        return introduce.getId();
    }

    @Override
    public Map<String, Object> searchIntroduceAndMasterByKeyword(String keyword, Member requestMember) {
        List<FindIntroduceResponse> introduceList = introduceRepository.searchIntroduceByKeywordForMember(keyword, requestMember.getId())
                .stream()
                .flatMap(introduce -> introduce.getQuestions().stream()
                        .filter(q -> q.getContent().contains(keyword))
                        .map(q -> FindIntroduceResponse.builder()
                                .introId(introduce.getId())
                                .title(introduce.getRecruit().getTitle())
                                .content(q.getContent())
                                .createdDate(introduce.getCreatedAt().toLocalDate())
                                .build()))
                .collect(Collectors.toList());

        List<FindMasterIntroduceResponse> masterIntroduceList = masterIntroduceRepository.searchMasterIntroduceByKeywordForMember(keyword, requestMember.getId())
                .stream()
                .flatMap(masterIntroduce -> masterIntroduce.getMasterQuestion().stream()
                        .filter(mq -> mq.getContent().contains(keyword))
                        .map(mq -> FindMasterIntroduceResponse.builder()
                                .masterIntroId(masterIntroduce.getId())
                                .title("Master")
                                .content(mq.getContent())
                                .createdDate(masterIntroduce.getCreatedAt().toLocalDate())
                                .build()))
                .collect(Collectors.toList());

        List<Object> result = new ArrayList<>();
        result.addAll(masterIntroduceList);
        result.addAll(introduceList);

        int count = result.size();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("count", count);
        response.put("data", result);

        return response;
    }

    @Override
    @Transactional
    public int findStateByRecruitId(Long recruitId) {
        return introduceRepository.findByRecruitId(recruitId)
                .map(Introduce::getState)
                .orElse(0);
    }
}
