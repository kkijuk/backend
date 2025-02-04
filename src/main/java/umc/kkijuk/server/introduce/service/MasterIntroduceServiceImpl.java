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

        // 상태 업데이트
        masterIntroduce.setState(introduceReqDto.getState());

        // 한줄 소개 업데이트
        masterIntroduce.setOneLiner(introduceReqDto.getOneLiner());

        List<MasterQuestion> existingQuestions = masterIntroduce.getMasterQuestion();
        Map<Integer, MasterQuestion> existingQuestionsMap = existingQuestions.stream()
                .collect(Collectors.toMap(MasterQuestion::getNumber, q -> q));

        List<QuestionDto> questionDtos = introduceReqDto.getQuestionList();
        List<MasterQuestion> updatedQuestions = new ArrayList<>();

        for (QuestionDto questionDto : questionDtos) {
            Integer number = questionDto.getNumber();
            MasterQuestion existingQuestion = existingQuestionsMap.get(number);

            if (existingQuestion != null) {
                existingQuestion.update(questionDto.getTitle(), questionDto.getContent());
                updatedQuestions.add(existingQuestion);
            } else {
                MasterQuestion newQuestion = new MasterQuestion();
                newQuestion.setTitle(questionDto.getTitle());
                newQuestion.setContent(questionDto.getContent());
                newQuestion.setNumber(questionDto.getNumber());
                newQuestion.setMasterIntroduce(masterIntroduce);
                updatedQuestions.add(newQuestion);
            }
        }

        List<MasterQuestion> toRemove = existingQuestions.stream()
                .filter(q -> questionDtos.stream().noneMatch(dto -> dto.getNumber() == q.getNumber()))
                .collect(Collectors.toList());

        for (MasterQuestion question : toRemove) {
            masterIntroduce.getMasterQuestion().remove(question);
            masterQuestionRepository.delete(question);
        }

        // 기존 질문 리스트에 업데이트된 질문들 추가
        masterIntroduce.getMasterQuestion().clear();
        masterIntroduce.getMasterQuestion().addAll(updatedQuestions);

        // masterIntroduce 저장
        masterIntroduce = masterIntroduceRepository.save(masterIntroduce);

        // 응답용 DTO 생성
        List<QuestionDto> responseQuestionList = updatedQuestions.stream()
                .map(question -> QuestionDto.builder()
                        .title(question.getTitle())
                        .content(question.getContent())
                        .number(question.getNumber())
                        .build())
                .collect(Collectors.toList());

        return MasterIntroduceResponse.builder()
                .masterIntroduce(masterIntroduce)
                .questionList(responseQuestionList)
                .build();
    }
}
