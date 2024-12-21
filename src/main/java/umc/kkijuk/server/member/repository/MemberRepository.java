package umc.kkijuk.server.member.repository;

import umc.kkijuk.server.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
    Member save(Member member);
    Optional<Member> findByPhoneNumber(String phoneNumber);
//    Optional<Member> findBySocialId(Long socialId);
    Optional<Member> findBySocialId(String socialId);
    void deleteById(Long id);
}