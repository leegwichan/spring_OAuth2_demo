package com.codestates.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Override
    // 로그인 후, 후처리 담당하는 메서드
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        System.out.println("userRequest : " + userRequest);

        // PrincipalDetailService를 보니, Member를 저장하는 메서드를 만들어야 할듯...
//        Member member = new Member();
//        member.setUsername(userRequest.getClientRegistration().get);

        // userRequest에 담긴 정보를 확인할 수 있는 메서드
        // userRequest.getClientRegistration();
        // userRequest.getAccessToken().getTokenValue()
        // super.loadUser(userRequest).getAttributes()

        return super.loadUser(userRequest);
    }
}
