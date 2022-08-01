package com.codestates.config;

import com.codestates.auth.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable(); // form 태그로만 요청 가능, postman 등의 요청이 불가능
        http.headers().frameOptions().disable(); // h2 연결시 필요
        http.formLogin().loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/"); // formLogin 설정
        http.authorizeRequests() // 접근 제한할 URI 설정
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .oauth2Login().loginPage("/login") // OAuth2 로그인 페이지 설정
                .userInfoEndpoint().userService(principalOauth2UserService); // OAuth2 로그인에 사용할 DefaultOAuth2UserService 설정
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
