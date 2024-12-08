//package umc.kkijuk.server.unitTest.mock;
//
//import lombok.Builder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import umc.kkijuk.server.member.repository.MemberRepository;
//import umc.kkijuk.server.member.service.MemberService;
//import umc.kkijuk.server.member.service.MemberServiceImpl;
//import umc.kkijuk.server.recruit.controller.port.RecruitService;
//import umc.kkijuk.server.recruit.service.RecruitServiceImpl;
//import umc.kkijuk.server.recruit.service.port.RecruitRepository;
//import umc.kkijuk.server.review.service.port.ReviewRepository;
//
//public class TestContainer {
//    public final RecruitRepository recruitRepository;
//    public final MemberRepository memberRepository;
//    public final ReviewRepository reviewRepository;
//
//    public final RecruitService recruitService;
//    public final MemberService memberService;
//
//    public final PasswordEncoder passwordEncoder;
//
//    public final LoginService loginService;
//
//
//    @Builder
//    public TestContainer() {
//        this.recruitRepository = new FakeRecruitRepository();
//        this.memberRepository = new FakeMemberRepository();
//        this.reviewRepository = new FakeReviewRepository();
//
//        this.passwordEncoder = new FakePasswordEncoder();
//        this.recruitService = RecruitServiceImpl.builder()
//                .recruitRepository(recruitRepository)
//                .build();
//        this.memberService = MemberServiceImpl.builder()
//                .memberRepository(memberRepository)
//                .passwordEncoder(passwordEncoder)
//                .build();
//        this.loginService = LoginService.builder()
//                .memberRepository(memberRepository)
//                .passwordEncoder(passwordEncoder)
//                .build();
//    }
//}
