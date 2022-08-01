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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // index.html 을 띄움
    @GetMapping("/")
    public String index(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {

        try {
            if(principalDetails.getUsername() != null) {
                model.addAttribute("username", principalDetails.getUsername());
            }
        } catch (NullPointerException e) {}
        return "index";
    }

    // 접근 제한이 걸린 API
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

    // 로그인 메서드
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
    public @ResponseBody String loginTest(Authentication authentication,
                                          @AuthenticationPrincipal PrincipalDetails principalDetails,
                                          @AuthenticationPrincipal OAuth2User oauth,
                                          Model model){
        System.out.println("##### login Test #####");

        // 로그인한 유저의 정보를 확인 가능 (PrincipalDetails 에서 정보 가져옴)
        PrincipalDetails getPrincipalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("member : " + getPrincipalDetails.getMember());
        System.out.println("member : " + principalDetails.getMember());

        // OAuth2 로그인한 유저의 정보 (Google 토큰 정보) 확인 가능
        // formLogin한 PrincipalDetails는 attribute를 이용하지 않음
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authenticaion : " + oAuth2User.getAttributes());
        System.out.println("authenticaion : " + oauth.getAttributes());

        return "console 창 및 IndexController.loginTest() 확인";
    }
}
