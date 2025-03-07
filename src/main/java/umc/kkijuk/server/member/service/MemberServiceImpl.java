package umc.kkijuk.server.member.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.kkijuk.server.auth.dto.NaverUserResponse;
import umc.kkijuk.server.auth.jwt.JwtUtil;
import umc.kkijuk.server.auth.service.TokenService;
import umc.kkijuk.server.common.domian.exception.*;
import umc.kkijuk.server.common.domian.status.AuthErrorStatus;
import umc.kkijuk.server.member.controller.response.MemberEmailResponse;
import umc.kkijuk.server.member.controller.response.MemberInfoResponse;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.domain.Role;
import umc.kkijuk.server.member.domain.SocialType;
import umc.kkijuk.server.member.domain.State;
import umc.kkijuk.server.member.dto.*;
import umc.kkijuk.server.member.repository.MemberRepository;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private final TokenService tokenService;
    private final JwtUtil jwtUtil;

    @Override
    public Member getById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", memberId));
    }

    @Override
    public List<String> getMemberField(Long memberId){
        Member member = this.getById(memberId);
        return member.getField();
    }

    @Override
    public MemberEmailResponse getMemberEmail(Member member) {
        if(member.getEmail() == null){
            throw new InvalidMemberDataException();
        }
        String maskEmail = maskEmail(member.getEmail());

        SocialType socialType = member.getSocialType();
        return MemberEmailResponse.builder()
                .email(maskEmail)
                .socialType(socialType)
                .build();
    }

    private static String maskEmail(String email) {
        String[] parts = email.split("@");
        if (parts.length != 2) {
            throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다.");
        }

        String localPart = parts[0];
        String domainPart = parts[1];

        String maskedLocalPart = localPart.length() > 2
                ? localPart.substring(0, 2) + "*".repeat(localPart.length() - 2)
                : localPart;

        return maskedLocalPart + "@" + domainPart;
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
        if (memberInfoChangeDto.getBirthDate().isBefore(LocalDate.of(1950, 1, 1)) ||
                memberInfoChangeDto.getBirthDate().isAfter(LocalDate.now())) {
            throw new InvalidBirthDateException();
        }
        member.changeMemberInfo(memberInfoChangeDto.getEmail(), memberInfoChangeDto.getPhoneNumber(), memberInfoChangeDto.getBirthDate(), memberInfoChangeDto.getMarketingAgree());
        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public List<String> addRecruitTag(Member member, String tag) {
        if (member.getRecruitTags() == null) {
            member.setRecruitTags(new ArrayList<>());
        }

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


    @Override
    @Transactional
    public Member createUserWithKakaoId(String kakaoId, Map<String, Object> kakaoUserInfo) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) kakaoUserInfo.get("kakao_account");
        String email = (String) kakaoAccount.get("email");
        String name = (String) kakaoAccount.get("name");
        String phoneNumber = (String) kakaoAccount.get("phone_number");
        String birthday = (String) kakaoAccount.get("birthday");
        String birthyear = (String) kakaoAccount.get("birthyear");


        LocalDate birthDate = null;
        if (birthday != null && !birthday.isEmpty()) {
            int year = (birthyear != null && !birthyear.isEmpty())
                    ? Integer.parseInt(birthyear)
                    : LocalDate.now().getYear();
            int month = Integer.parseInt(birthday.substring(0, 2));
            int day = Integer.parseInt(birthday.substring(2, 4));
            birthDate = LocalDate.of(year, month, day);
        }

        if (phoneNumber != null && phoneNumber.startsWith("+82")) {
            phoneNumber = phoneNumber.replace("+82 ", "0");
        }

        Member newMember = new Member();
        newMember.setSocialId(kakaoId);
        newMember.setEmail(email);
        newMember.setName(name);
        newMember.setPhoneNumber(phoneNumber);
        newMember.setBirthDate(birthDate);
        newMember.setRole(Role.ROLE_USER);
        newMember.setSocialType(SocialType.KAKAO);
        newMember.setProfileComplete(false);
        newMember.setUserState(State.ACTIVATE);


        log.info("신규 사용자 생성 - Kakao ID: {}, 이메일: {}, 이름: {}, 전화번호: {}, 생년월일: {}", kakaoId, email, name, phoneNumber, birthDate);
        return memberRepository.save(newMember);
    }
    @Override
    @Transactional
    public MemberInfoResponse getMemberInfo(Long memberId) {
        Member member = this.getById(memberId);
        return MemberInfoResponse.builder()
                .socialId(member.getSocialId())
                .email(member.getEmail())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .birthDate(member.getBirthDate())
                .role(member.getRole())
                .marketingAgree(member.getMarketingAgree())
                .socialType(member.getSocialType())
                .build();
    }

    @Transactional
    public Member createUserWithNaverId(String naverId, NaverUserResponse.NaverUserDetail naverUserInfo) {
        String email = naverUserInfo.getEmail();
        String name = naverUserInfo.getName();
        String phoneNumber = naverUserInfo.getMobile();
        String birthday = (String) naverUserInfo.getBirthday();
        String birthyear = (String) naverUserInfo.getBirthyear();

        LocalDate birthDate = null;
        if (birthday != null && !birthday.isEmpty()) {
            int year = (birthyear != null && !birthyear.isEmpty())
                    ? Integer.parseInt(birthyear)
                    : LocalDate.now().getYear();
            String[] dateParts = birthday.split("-");
            int month = Integer.parseInt(dateParts[0]);
            int day = Integer.parseInt(dateParts[1]);

            birthDate = LocalDate.of(year, month, day);
        }

        Member newMember = new Member();
        newMember.setSocialId(naverId);
        newMember.setEmail(email);
        newMember.setName(name);
        newMember.setPhoneNumber(phoneNumber);
        newMember.setBirthDate(birthDate);
        newMember.setRole(Role.ROLE_USER);
        newMember.setSocialType(SocialType.NAVER);
        newMember.setUserState(State.ACTIVATE);
        newMember.setProfileComplete(false);


        log.info("신규 사용자 생성 - Naver ID: {}, 이메일: {}, 이름: {}, 전화번호: {}, 생년월일: {}", naverId, email, name, phoneNumber, birthDate);
        return memberRepository.save(newMember);
    }

    @Override
    @Transactional
    public Member completeProfile(Long memberId, ProfileInputDto profileInputDto) {
        Member member = this.getById(memberId);
        member.setTermsAgree(profileInputDto.getIsTermsAgreed());
        member.setPrivacyAgree(profileInputDto.getIsPrivacyAgreed());
        member.setMarketingAgree(profileInputDto.getIsMarketingAgreed());
        member.setMemberJob(profileInputDto.getMemberJob());
        member.setProfileComplete(true);
        return memberRepository.save(member);
    }


    @Override
    @Transactional
    public Long extractMemberId(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization 헤더에 올바른 토큰이 없습니다.");
        }

        String socialId = jwtUtil.extractId(bearerToken.substring(7));
        return this.findBySocialId(String.valueOf(socialId)).getId();
    }

    @Override
    @Transactional
    public Member findBySocialId(String SocialId) {
        return memberRepository.findBySocialId(SocialId)
                .orElseThrow(() -> new RuntimeException("Member not found with Social ID: " + SocialId));
    }

    @Override
    @Transactional
    public void memberInactivation(Long memberId, String token) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomAuthException(AuthErrorStatus.USER_NOT_FOUND));
        String socialId = jwtUtil.extractId(token.substring(7));
        member.inactivate();
        tokenService.invalidateRefreshToken(socialId);
        memberRepository.save(member);
    }

    //매일 자정 확인 후 유저 정보 지우는 함수, 실제로 자정에 삭제 되는지는 배포 후 확인 필요
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void deleteScheduledMembers() {
        Optional<List<Member>> optionalMembersToDelete = memberRepository.findByDeleteDateBefore(LocalDate.now());

        if (optionalMembersToDelete.isPresent()) {
            List<Member> membersToDelete = optionalMembersToDelete.get();
            for (Member member : membersToDelete) {
                memberRepository.deleteById(member.getId());
            }
        }
    }

}

