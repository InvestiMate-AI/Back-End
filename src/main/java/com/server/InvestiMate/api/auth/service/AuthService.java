package com.server.InvestiMate.api.auth.service;

import com.server.InvestiMate.api.auth.dto.response.AuthTokenResponseDto;
import com.server.InvestiMate.api.member.domain.Member;
import com.server.InvestiMate.api.member.domain.RoleType;
import com.server.InvestiMate.api.member.repository.MemberRepository;
import com.server.InvestiMate.common.jwt.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    public AuthTokenResponseDto getNewToken(String refreshToken) {
        // 리프레쉬 토큰 검증 후 유효하지 않으면 로그인 화면으로
        jwtUtil.validateToken(refreshToken);
        // 클라이언트로 부터 받은 리프레쉬 토큰으로 멤버 검색
        memberRepository.findMemberByRefreshTokenOrThrow(refreshToken);

        Long memberIdRefresh = jwtUtil.getMemberId(refreshToken);
        RoleType roleRefresh = jwtUtil.getRole(refreshToken);

        // 새로운 엑세스 토큰 생성
        String newAccessToken = jwtUtil.generateToken("access", memberIdRefresh, String.valueOf(roleRefresh), jwtUtil.accessTokenExpireLength);

        // 새로운 엑세스 토큰을 포함한 DTO 반환
        return new AuthTokenResponseDto(newAccessToken);
    }

    public void validateRefreshAndLogout(String refreshToken) {
        //DB에 저장되어 있는지 확인. 없면 예외!
        Member byRefreshTokenOrThrow = memberRepository.findMemberByRefreshTokenOrThrow(refreshToken);
        //로그아웃 진행
        //Refresh 토큰 DB에서 제거
        byRefreshTokenOrThrow.updateRefreshToken(null);
    }
}
