package umc.kkijuk.server.introduce.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.kkijuk.server.common.domian.exception.IntroFoundException;
import umc.kkijuk.server.common.domian.exception.IntroOwnerMismatchException;
import umc.kkijuk.server.common.domian.exception.ResourceNotFoundException;
import umc.kkijuk.server.introduce.controller.response.MasterIntroduceResponse;
import umc.kkijuk.server.introduce.domain.MasterIntroduce;
import umc.kkijuk.server.introduce.domain.MasterQuestion;
import umc.kkijuk.server.introduce.dto.IntroduceReqDto;
import umc.kkijuk.server.introduce.dto.QuestionDto;
import umc.kkijuk.server.introduce.repository.MasterIntroduceRepository;
import umc.kkijuk.server.introduce.repository.MasterQuestionRepository;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MasterIntroduceServiceImpl implements MasterIntroduceService {

    private final MasterIntroduceRepository masterIntroduceRepository;
    private final MasterQuestionRepository masterQuestionRepository;

    @Override
    @Transactional
    public MasterIntroduceResponse saveMasterIntro(Long memberId, IntroduceReqDto introduceReqDto) {
        if (masterIntroduceRepository.findByMemberId(memberId).isPresent()) {
            throw new IntroFoundException("이미 자기소개서가 존재합니다");
        }

        List<MasterQuestion> masterQuestions = introduceReqDto.getQuestionList().stream()
                .map(dto -> new MasterQuestion(dto.getTitle(), dto.getContent(), dto.getNumber()))
                .collect(Collectors.toList());

        MasterIntroduce masterIntroduce = MasterIntroduce.builder()
                .memberId(memberId)
                .masterQuestion(masterQuestions)
                .state(introduceReqDto.getState())
                .build();

        masterIntroduceRepository.save(masterIntroduce);

        return new MasterIntroduceResponse(masterIntroduce, introduceReqDto.getQuestionList());
    }

    @Override
    @Transactional
    public MasterIntroduceResponse getMasterIntro(Long memberId) {
        MasterIntroduce masterIntroduce = masterIntroduceRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("MasterIntroduce", memberId));
        if (!masterIntroduce.getMemberId().equals(memberId)) {
            throw new IntroOwnerMismatchException();
        }
        List<QuestionDto> masterQuestionList = masterIntroduce.getMasterQuestion()
                .stream()
                .map(question -> new QuestionDto(question.getTitle(), question.getContent(), question.getNumber()))
                .collect(Collectors.toList());
        return new MasterIntroduceResponse(masterIntroduce, masterQuestionList);
    }

    @Override
    @Transactional
    public MasterIntroduceResponse updateMasterIntro(Long memberId, IntroduceReqDto introduceReqDto) throws Exception {
        MasterIntroduce masterIntroduce = masterIntroduceRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("masterIntroduce", memberId));

        if (!masterIntroduce.getMemberId().equals(memberId)) {
            throw new IntroOwnerMismatchException();
        }

        // 상태 및 한줄소개 업데이트
        masterIntroduce.setState(introduceReqDto.getState());
        masterIntroduce.setOneLiner(introduceReqDto.getOneLiner());

        // 기존 질문 Map 구성 (중복 키 방지)
        Map<Integer, MasterQuestion> existingQuestionsMap = new HashMap<>();
        for (MasterQuestion q : masterIntroduce.getMasterQuestion()) {
            existingQuestionsMap.putIfAbsent(q.getNumber(), q);
        }

        // 요청으로 들어온 질문 리스트 기준으로 수정 및 추가
        for (QuestionDto dto : introduceReqDto.getQuestionList()) {
            Integer number = dto.getNumber();
            MasterQuestion question = existingQuestionsMap.get(number);
            if (question != null) {
                question.update(dto.getTitle(), dto.getContent());
            } else {
                MasterQuestion newQuestion = new MasterQuestion();
                newQuestion.setTitle(dto.getTitle());
                newQuestion.setContent(dto.getContent());
                newQuestion.setNumber(number);
                newQuestion.setMasterIntroduce(masterIntroduce);
                masterIntroduce.getMasterQuestion().add(newQuestion);
            }
        }

        // 제거할 질문 필터링
        List<MasterQuestion> toRemove = new ArrayList<>();
        for (MasterQuestion q : new ArrayList<>(masterIntroduce.getMasterQuestion())) {
            boolean existsInDto = introduceReqDto.getQuestionList().stream()
                    .anyMatch(dto -> dto.getNumber() == (q.getNumber()));
            if (!existsInDto) {
                toRemove.add(q);
            }
        }

        // 실제 제거
        toRemove.forEach(q -> {
            masterIntroduce.getMasterQuestion().remove(q);
            masterQuestionRepository.delete(q);
        });

        // updatedAt 수동 업데이트
        masterIntroduce.updateTimestamp();

        // 응답 DTO 생성
        List<QuestionDto> responseQuestionList = masterIntroduce.getMasterQuestion().stream()
                .map(q -> QuestionDto.builder()
                        .title(q.getTitle())
                        .content(q.getContent())
                        .number(q.getNumber())
                        .build())
                .sorted(Comparator.comparingInt(QuestionDto::getNumber))
                .collect(Collectors.toList());

        return MasterIntroduceResponse.builder()
                .masterIntroduce(masterIntroduce)
                .questionList(responseQuestionList)
                .build();
    }

}
