package com.codestates.controller;

import com.codestates.auth.PrincipalDetails;
import com.codestates.model.Member;
import com.codestates.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/")
    public @ResponseBody String index() {
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user() {
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    @GetMapping("/login")
    public String login() {
        return "loginForm";
    }

    @GetMapping("/join")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(Member member) {
        member.setRole("ROLE_USER");
        String rawPassword = member.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        member.setPassword(encPassword);

        memberRepository.save(member);

        return "redirect:/login";
    }

    @GetMapping("/loginTest")
    public @ResponseBody String loginTest(Authentication authentication){
        System.out.println("##### login Test1 #####");

        if (authentication.getPrincipal() instanceof PrincipalDetails == false){
            return "당신은 OAuth2 로그인 하였습니다. \"loginTest3\" 로 이동하세요";
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication : " + principalDetails.getMember());
        return "세션 정보 확인";
    }

    @GetMapping("/loginTest2")
    public @ResponseBody String loginTest2(@AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("##### login Test2 #####");

        if (principalDetails == null){
            return "당신은 OAuth2 로그인 하였습니다. \"loginTest3\" 로 이동하세요";
        }

        System.out.println("userDetails : " + principalDetails.getMember());
        return "세션 정보 확인2";
    }

    @GetMapping("/loginTest3")
    public  @ResponseBody String loginOAuthTest(Authentication authentication,
                                                @AuthenticationPrincipal OAuth2User oauth){
        System.out.println("##### OAuth login Test #####");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authenticaion : " + oAuth2User.getAttributes());
        System.out.println("oauth2User : " + oauth.getAttributes());
        return "세션 정보 확인3";
    }
}
