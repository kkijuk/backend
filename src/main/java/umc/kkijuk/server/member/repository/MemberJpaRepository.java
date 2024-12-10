package umc.kkijuk.server.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.kkijuk.server.member.domain.Member;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long>{
    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByPhoneNumber(String phoneNumber);
}
