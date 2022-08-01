# spring_OAuth2_demo
google 소셜 로그인 구현
- application.yml 에 [Google API Console](https://console.cloud.google.com/apis)에서 만든 clientId, clientSecret를 넣어야 함
---
- auth
  - PrincipalDetails
    - POST "/login" 주소에 요청이 오면 대신 로그인(username, password) 을 진행
    - implements UserDetails, OAuth2User
      - formlogin을 통해서는 UserDetails에 정보를 담고, OAuth2를 통해서는 OAuth2User에 정보를 담음
      - 이를 다른 Class로 관리하는 것은 까다로움 (authentication.getPrincipal()로 객체를 가져오는데, 상황마다 다른 객체를 써야함)
      - OAuth2User에서는 따로 Member의 정보를 담고 있지 않음
      - 따라서 PrincipalDetails를 이용해서
    - Controller 등에서 authentication.getPrincipal() 을 이용하여 유저 정보를 가져올 수 있음
  
  - PrincipalDetailsService
    - Security 설정에서 loginProcessingUrl(”/login”);으로 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 메서드 실행
    - PrincipalDetails(UserDetails)를 만들어 return함 
    - implements UserDetailsService
    - @Service를 통해 Spring Bean에 등록
  
  - oauth.PrincipalOauth2UserService
    - OAuth2User 객체를 만드는데 사용하는 객체
    - loadUser : OAuth2 로그인 후처리를 담당하는 메서드
      - DB에 회원 내용이 없다면, 회원 내용을 DB에 저장
    - PrincipalDetails(OAuth2User) 를 만들어 return함

- config
  - SecurityConfig
    - 기본적인 Spring Security 설정
    - OAuth2 로그인 설정, PrincipalOauth2UserService 이용한다는 내용 추가

  - WebMvcConfig
    - mustache → html 사용할 수 있도록 설정

- controller.IndexController
  - GET "/user", "/admin", "/manager" : SecurityConfig 에서 권한에 따라 접근을 제한한 API
  - GET "/login" : 로그인 페이지를 띄움, "/join" : 회원가입 페이지 (username, password)를 띄움
  - GET "/" : 메인 페이지를 띄움 (로그인 상태 확인 가능)
  - GET "loginTest" : 유저 정보 가져오는 메서드 확인 가능 (console 창에서 확인)
  - 이외 메서드는 직접 확인

- model : 회원 Entity
- repository : 회원 저장하는 repository