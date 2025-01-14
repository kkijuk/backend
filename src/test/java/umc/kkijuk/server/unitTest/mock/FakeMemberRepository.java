//package umc.kkijuk.server.unitTest.mock;
//
//import umc.kkijuk.server.member.domain.Member;
//import umc.kkijuk.server.member.repository.MemberRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.concurrent.atomic.AtomicLong;
//
//public class FakeMemberRepository implements MemberRepository {
//    private final AtomicLong authGeneratedID = new AtomicLong(0);
//    private final List<Member> data = new ArrayList<Member>();
//
//    @Override
//    public Member save(Member member) {
//        if (member.getId() == null || member.getId() == 0){
//            Member newMember = Member.builder()
//                    .id(authGeneratedID.incrementAndGet())
//                    .email(member.getEmail())
//                    .name(member.getName())
//                    .birthDate(member.getBirthDate())
//                    .phoneNumber(member.getPhoneNumber())
//                    .field(member.getField())
//                    .marketingAgree(member.getMarketingAgree())
//                    .userState(member.getUserState())
//                    .deleteDate(member.getDeleteDate())
//                    .build();
//            data.add(newMember);
//            return newMember;
//        } else {
//            data.removeIf(item -> Objects.equals(item.getId(), member.getId()));
//            data.add(member);
//            return member;
//        }
//    }
//
//    @Override
//    public Optional<Member> findById(Long id) {
//        return data.stream()
//                .filter(item -> item.getId().equals(id))
//                .findAny();
//    }
//
//    @Override
//    public Optional<Member> findByEmail(String email) {
//        return data.stream()
//                .filter(item -> item.getEmail().equals(email))
//                .findAny();
//    }
//}
