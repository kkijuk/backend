package umc.kkijuk.server.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.kkijuk.server.common.domian.exception.ConfirmPasswordMismatchException;
import umc.kkijuk.server.common.domian.exception.ResourceNotFoundException;
import umc.kkijuk.server.member.controller.response.CreateMemberResponse;
import umc.kkijuk.server.member.domain.Member;
import umc.kkijuk.server.member.dto.MemberFieldDto;
import umc.kkijuk.server.member.dto.MemberInfoChangeDto;
import umc.kkijuk.server.member.dto.MemberJoinDto;
import umc.kkijuk.server.member.repository.MemberJpaRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberJpaRepository memberJpaRepository;

    public Member findOne(Long memberId) {
        return memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", memberId));
    }


    //아직 인터페이스 구현 x, 추후에 구현 후 MemberService -> MemberServiceImpl로 수정 예정.
    @Transactional
    public Member join(MemberJoinDto memberJoinDto) {

        String passwordConfirm = memberJoinDto.getPasswordConfirm();
        if (!passwordConfirm.equals(memberJoinDto.getPassword())) {
            throw new ConfirmPasswordMismatchException();
        }

        Member joinMember = memberJoinDto.toEntity();
        memberJpaRepository.save(joinMember);
        return joinMember;
    }

    /* security 의존성 추가후 변경할 join 함수 */
//    @Transactional
//    public Long join(Member member) {
//        member.changeMemberPassword(passwordEncoder.encode(member.getPassword()));
//        memberJpaRepository.save(member);
//        return member.getId();
//    }

    public Member getMemberInfo(Long memberId) {
        return memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }

    public List<String> getMemberField(Long memberId){
        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        return member.getField();
    }

    @Transactional
    public List<String> updateMemberField(Long id,MemberFieldDto memberFieldDto){
        Member member = memberJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        member.changeFieldInfo(memberFieldDto.getField());
        return member.getField();
    }

    @Transactional
    public Long updateMemberInfo(Long id,MemberInfoChangeDto memberInfoChangeDto){
        Member member = memberJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        member.changeMemberInfo(memberInfoChangeDto.getPhoneNumber(), memberInfoChangeDto.getBirthDate(), memberInfoChangeDto.getMarketingAgree());
        return member.getId();
    }


}
