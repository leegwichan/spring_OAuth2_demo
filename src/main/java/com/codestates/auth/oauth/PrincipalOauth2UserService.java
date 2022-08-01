package com.codestates.auth.oauth;

import com.codestates.auth.PrincipalDetails;
import com.codestates.model.Member;
import com.codestates.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    // 로그인 후, 후처리 담당하는 메서드
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{

        // userRequest에 담긴 정보를 확인할 수 있는 메서드
        // userRequest.getClientRegistration();
        // userRequest.getAccessToken().getTokenValue()
        // super.loadUser(userRequest).getAttributes()

        OAuth2User oauth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getClientId();
        String providerId = oauth2User.getAttribute("sub");
        String username = oauth2User.getAttribute("name");
        String email = oauth2User.getAttribute("email");
        String role = "ROSE_USER";

        Member memberEntity = memberRepository.findByUsername(username);

        if(memberEntity == null) {
            // OAuth로 처음 로그인한 유저 - 회원가입 처리
            memberEntity = Member.builder()
                    .username(username)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            memberRepository.save(memberEntity);
        }

        return new PrincipalDetails(memberEntity, oauth2User.getAttributes());
    }
}
