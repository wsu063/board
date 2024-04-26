package com.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // 1. 페이지 접근에 대한 설정(인가)
        httpSecurity.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/css/**", "/js/**", "/img/**", "/images/**", "/fonts/**").permitAll()
                .requestMatchers("/", "/members/**", "/item/**").permitAll()
                .requestMatchers("/favicon.ico", "/error").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        )        // 2. 로그인에 관한 설정
        .formLogin(formLogin -> formLogin
                .loginPage("/members/login") // 로그인 페이지 URL
                .defaultSuccessUrl("/") // 로그인 성공시 이동할 페이지 URL
                .usernameParameter("email") // ★로그인시 id로 사용할 파라미터 이름(내 사이트에 맞는걸로)
                .failureUrl("/members/login/error") // 로그인 실패시 이동할 페이지
        )        // 3. 로그아웃에 관한 설정
        .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) //로그아웃시 이동할 URL
                .logoutSuccessUrl("/") // 로그아웃 성공시 이동할 URL
        ) // 4. 인증되지 않은 사용자의 접근에 관한 설정
        .exceptionHandling(handling -> handling
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        ) // 로그인 이후 세션을 통해 로그인 유지
        .rememberMe(Customizer.withDefaults());

        return httpSecurity.build();
        
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
