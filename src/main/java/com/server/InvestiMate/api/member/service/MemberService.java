package com.server.InvestiMate.api.member.service;

import com.server.InvestiMate.api.member.domain.Member;
import com.server.InvestiMate.api.member.dto.request.MemberSaveProfileDto;
import com.server.InvestiMate.api.member.dto.response.MemberGetProfileResponseDto;
import com.server.InvestiMate.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberGetProfileResponseDto getMemberProfile(String oAuth2Id) {
        Member member = memberRepository.findByOAuth2IdOrThrow(oAuth2Id);
        return MemberGetProfileResponseDto.of(member);
    }

    public void updateRefreshToken(String oAuth2Id, String refreshToken) {
        Member member = memberRepository.findByoAuth2Id(oAuth2Id)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        member.updateRefreshToken(refreshToken);
        memberRepository.save(member);
    }

    public void saveMemberProfile(String oAuth2Id, MemberSaveProfileDto memberSaveProfileDto) {
        Member byoAuth2Id = memberRepository.findByOAuth2IdOrThrow(oAuth2Id);
        String nickname = memberSaveProfileDto.nickname();
        String memberIntro = memberSaveProfileDto.memberIntro();
        byoAuth2Id.updateMemberProfile(nickname, memberIntro);
    }
}
