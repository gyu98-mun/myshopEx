package com.example.myShop.config;

import com.example.myShop.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final MemberService memberService;

    public SecurityConfig(MemberService memberService) {
        this.memberService = memberService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() { // 해시함수 이용하여 비밀번호 암호화하여 저장
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/members/login") // login page URL 설정
                .defaultSuccessUrl("/") // 로그인 성공 시 이동할 URL
                .usernameParameter("email") // 로그인 시 사용할 파라미터 이름 지정
                .failureUrl("/members/login/error") //로그인 실패 시 이동할 URL
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) // 로그아웃 URL
                .logoutSuccessUrl("/"); // 로그아웃 성공 시 이동할 URL

        http.authorizeRequests() // 시큐리티 처리에 HttpServletRequest를 이용
                .mvcMatchers("/","/members/**", "/item/**","/images/**").permitAll()
            .mvcMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated(); // 앞서 설정해준 경로를 제외한 나머지 경로는 인증을 요구

        http.exceptionHandling() // 인증되지 않은 사용자가 리소스 접근 시 수행되는 핸들러 등록
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

    }

    @Override
    protected  void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(memberService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception{ // static 디렉토리 하위파일은 인증 무시
        web.ignoring().antMatchers("/css/**","/js/**","/img/**");
    }
}

