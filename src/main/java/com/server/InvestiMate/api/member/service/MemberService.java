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

    public MemberGetProfileResponseDto getMemberProfile(Long memberId) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        return MemberGetProfileResponseDto.of(member);
    }

    public void updateRefreshToken(Long memberId, String refreshToken) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        member.updateRefreshToken(refreshToken);
        memberRepository.save(member);
    }

    public void saveMemberProfile(Long memberId, MemberSaveProfileDto memberSaveProfileDto) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        String nickname = memberSaveProfileDto.nickname();
        String memberIntro = memberSaveProfileDto.memberIntro();
        member.updateMemberProfile(nickname, memberIntro);
    }
}
