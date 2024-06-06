package com.server.InvestiMate.api.auth.service;

import com.server.InvestiMate.api.auth.domain.CustomOAuth2User;
import com.server.InvestiMate.api.auth.dto.response.GoogleResponse;
import com.server.InvestiMate.api.auth.dto.response.NaverResponse;
import com.server.InvestiMate.api.auth.dto.response.OAuth2Response;
import com.server.InvestiMate.api.member.domain.Member;
import com.server.InvestiMate.api.member.domain.RoleType;
import com.server.InvestiMate.api.member.repository.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("--------------------------- CustomOAuth2UserService ---------------------------");
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // Provider 얻기
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2Response oAuth2Response = null;
        // 각 Provider에 따라 응답 규격이 다름.
        if (registrationId.equals("naver")) {oAuth2Response = new NaverResponse(oAuth2User.getAttributes());}
        else if (registrationId.equals("google")) {oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());}
        else {return null;}

        String oAuth2Id = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        String name = oAuth2Response.getName();
        String email = oAuth2Response.getEmail();

        Optional<Member> byoAuth2Id = memberRepository.findByoAuth2Id(oAuth2Id);
        Member savedMember = byoAuth2Id.orElseGet(() -> saveNewMember(oAuth2Id, name, email));

        return new CustomOAuth2User(savedMember);
    }

    private Member saveNewMember(String oAuth2Id, String name, String email) {
        Member newMember = Member.builder()
                .oAuth2Id(oAuth2Id)
                .name(name)
                .email(email)
                .roleType(RoleType.USER)
                .build();
        return memberRepository.save(newMember);
    }
}