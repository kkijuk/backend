package umc.kkijuk.server.login;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import umc.kkijuk.server.login.argumentresolver.LoginMemberArgumentResolver;
import umc.kkijuk.server.login.interceptor.CorsCookieInterceptor;
import umc.kkijuk.server.login.interceptor.LoginCheckInterceptor;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger-ui/**", "/v3/api-docs/**") // 스웨거 관련
                .excludePathPatterns("/health-check")
                .excludePathPatterns("/login", "/logout") // 로그인
                .excludePathPatterns("/api/error") // 에러처리
                .excludePathPatterns("/member", "/member/confirmEmail") // 회원가입, 회원가입 시 이메일 중복 확인
                .excludePathPatterns("/auth/**", "/password/**"); // 이메일 인증
//        registry.addInterceptor(new CorsCookieInterceptor())
//                .order(2)
//                .addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }
}
