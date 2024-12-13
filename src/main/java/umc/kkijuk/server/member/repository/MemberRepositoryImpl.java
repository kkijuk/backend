package umc.kkijuk.server.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import umc.kkijuk.server.member.domain.Member;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository{
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Optional<Member> findById(Long id) {
        return memberJpaRepository.findById(id);
    }
    @Override
    public Optional<Member> findByEmail(String email) {
        return memberJpaRepository.findByEmail(email);
    }

    @Override
    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }
    @Override
    public Optional<Member> findByPhoneNumber(String phoneNumber) {
        return memberJpaRepository.findByPhoneNumber(phoneNumber);
    }
    @Override
    public Optional<Member> findByKakaoId(Long kakaoId) {
        return memberJpaRepository.findByKakaoId(kakaoId);
    }
}