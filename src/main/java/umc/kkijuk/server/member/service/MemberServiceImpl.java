package umc.kkijuk.server.member.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.kkijuk.server.common.domian.exception.*;
import umc.kkijuk.server.member.controller.response.EmailAuthResponse;
import umc.kkijuk.server.member.controller.response.MemberEmailResponse;
import umc.kkijuk.server.member.controller.response.MemberInfoResponse;
import umc.kkijuk.server.member.controller.response.MemberStateResponse;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.domain.Role;
import umc.kkijuk.server.member.domain.State;
import umc.kkijuk.server.member.dto.*;
import umc.kkijuk.server.member.repository.MemberRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Builder
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member getById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", memberId));
    }

//    @Override
//    @Transactional
//    public Member join(MemberJoinDto memberJoinDto) {
//        String passwordConfirm = memberJoinDto.getPasswordConfirm();
//        if (!passwordConfirm.equals(memberJoinDto.getPassword())) {
//            throw new ConfirmPasswordMismatchException();
//        }
//
//        Member joinMember = memberJoinDto.toEntity();
//
//        String encodedPassword = passwordEncoder.encode(memberJoinDto.getPassword());
//        joinMember.changeMemberPassword(encodedPassword);
//
//        Optional<Member> member = memberRepository.findByEmail(memberJoinDto.getEmail());
//        if (member.isPresent()){
//            throw new EmailAlreadyExistsException();
//        }
//
//        return memberRepository.save(joinMember);
//    }

    @Override
    public MemberInfoResponse getMemberInfo(Long memberId) {
        Member member = this.getById(memberId);
        if(member.getEmail() == null || member.getName() == null || member.getPhoneNumber() == null || member.getBirthDate() == null){
            throw new InvalidMemberDataException();
        }
        return MemberInfoResponse.builder()
                .email(member.getEmail())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .birthDate(member.getBirthDate())
                .build();
    }

    @Override
    public List<String> getMemberField(Long memberId){
        Member member = this.getById(memberId);
        return member.getField();
    }

    @Override
    public MemberEmailResponse getMemberEmail(Long memberId) {
        Member member = this.getById(memberId);
        if(member.getEmail() == null){
            throw new InvalidMemberDataException();
        }
        return MemberEmailResponse.builder()
                .email(member.getEmail())
                .build();
    }

    @Override
    @Transactional
    public Member updateMemberField(Long memberId, MemberFieldDto memberFieldDto){
        Member member = this.getById(memberId);

        member.changeFieldInfo(memberFieldDto.getField());
        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public Member updateMemberInfo(Long memberId, MemberInfoChangeDto memberInfoChangeDto){
        Member member = this.getById(memberId);
        if(member.getPhoneNumber() == null || member.getBirthDate() == null || member.getMarketingAgree() == null){
            throw new InvalidMemberDataException();
        }
        member.changeMemberInfo(memberInfoChangeDto.getPhoneNumber(), memberInfoChangeDto.getBirthDate(), memberInfoChangeDto.getMarketingAgree());
        return memberRepository.save(member);
    }

//    @Override
//    @Transactional
//    public Member changeMemberPassword(Long memberId, MemberPasswordChangeDto memberPasswordChangeDto){
//        Member member = this.getById(memberId);
//        if(!memberPasswordChangeDto.getNewPassword().equals(memberPasswordChangeDto.getNewPasswordConfirm())){
//            throw new ConfirmPasswordMismatchException();
//        }
//        if(!passwordEncoder.matches(memberPasswordChangeDto.getCurrentPassword(), member.getPassword())){
//            throw new CurrentPasswordMismatchException();
//        }
//
//        String encodedPassword = passwordEncoder.encode(memberPasswordChangeDto.getNewPassword());
//        member.changeMemberPassword(encodedPassword);
//
//        return memberRepository.save(member);
//    }

//    @Override
//    public Member myPagePasswordAuth(Long memberId, MyPagePasswordAuthDto myPagePasswordAuthDto) {
//        Member member = this.getById(memberId);
//
//        if(!passwordEncoder.matches(myPagePasswordAuthDto.getCurrentPassword(), member.getPassword())){
//            throw new CurrentPasswordMismatchException();
//        }
//
//        return member;
//    }

    @Override
    @Transactional
    public MemberStateResponse changeMemberState(Long memberId){
        Member member = this.getById(memberId);
        if(member.getUserState().equals(State.INACTIVATE)){
            member.activate();
        }
        else if(member.getUserState().equals(State.ACTIVATE)){
            member.inactivate();
        }

        memberRepository.save(member);

        return MemberStateResponse.builder()
                .memberState(member.getUserState())
                .build();
    }

//    @Override
//    @Transactional
//    public Member resetMemberPassword(MemberPasswordResetDto memberPasswordResetDto){
//        Optional<Member> member = memberRepository.findByEmail(memberPasswordResetDto.getEmail());
//
//        if(!memberPasswordResetDto.getNewPassword().equals(memberPasswordResetDto.getNewPasswordConfirm())){
//            throw new ConfirmPasswordMismatchException();
//        }
//
//        String encodedPassword = passwordEncoder.encode(memberPasswordResetDto.getNewPassword());
//        member.get().changeMemberPassword(encodedPassword);
//
//        return memberRepository.save(member.get());
//    }

    @Override
    public Boolean confirmDupEmail(MemberEmailDto memberEmailDto) {
        Optional<Member> member = memberRepository.findByEmail(memberEmailDto.getEmail());
        return member.isEmpty();
    }

    @Override
    @Transactional
    public List<String> addRecruitTag(Member member, String tag) {
        List<String> recruitTags = member.getRecruitTags();
        if (recruitTags.contains(tag)) {
            throw new RecruitTagAlreadyExistException(tag);
        }

        member.addRecruitTag(tag);
        return member.getRecruitTags();
    }

    @Override
    @Transactional
    public List<String> deleteRecruitTag(Member member, String tag) {
        List<String> recruitTags = member.getRecruitTags();
        if (!recruitTags.contains(tag)) {
            throw new RecruitTagNotFoundException(tag);
        }

        member.deleteRecruitTag(tag);
        return member.getRecruitTags();
    }

    /**
     * 아래부터 소셜로그인 이후 추가된 기능
     */

    @Override
    @Transactional
    public Member createUserWithKakaoId(Long kakaoId, Map<String, Object> kakaoUserInfo) {
        // 카카오 사용자 정보에서 필요한 값 추출
        Map<String, Object> kakaoAccount = (Map<String, Object>) kakaoUserInfo.get("kakao_account");
        String email = (String) kakaoAccount.get("email");
        String name = (String) kakaoAccount.get("name");
        String phoneNumber = (String) kakaoAccount.get("phone_number");
        String birthday = (String) kakaoAccount.get("birthday"); // MMDD 형식
        String birthyear = (String) kakaoAccount.get("birthyear"); // YYYY 형식 (선택적)
        LocalDate birthDate = null;

        if (birthday != null && !birthday.isEmpty()) {
            int year = (birthyear != null && !birthyear.isEmpty())
                    ? Integer.parseInt(birthyear)
                    : LocalDate.now().getYear();
            int month = Integer.parseInt(birthday.substring(0, 2));
            int day = Integer.parseInt(birthday.substring(2, 4));
            birthDate = LocalDate.of(year, month, day);
        }

        // 새로운 사용자 생성 및 저장
        Member newMember = new Member();
        newMember.setKakaoId(kakaoId);
        newMember.setEmail(email);
        newMember.setName(name);
        newMember.setPhoneNumber(phoneNumber);
        newMember.setBirthDate(birthDate);
        newMember.setRole(Role.ROLE_USER);

        log.info("신규 사용자 생성 - 카카오 ID: {}, 이메일: {}, 이름: {}, 전화번호: {}, 생년월일: {}", kakaoId, email, name, phoneNumber, birthDate);

        return memberRepository.save(newMember);
    }

}
