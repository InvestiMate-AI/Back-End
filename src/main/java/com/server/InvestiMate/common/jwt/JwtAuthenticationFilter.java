package com.server.InvestiMate.common.jwt;

import com.server.InvestiMate.api.auth.domain.CustomOAuth2User;
import com.server.InvestiMate.common.jwt.exception.JwtExceptionType;
import com.server.InvestiMate.api.member.domain.Member;
import com.server.InvestiMate.api.member.domain.RoleType;
import com.server.InvestiMate.common.response.ErrorStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * JwtAuthenticationFilter를 거치지 않을 URL
     */
    private static final String[] whitelist = {
            "/api/v1/auth/reissue",
            "/token",
            "/login",
            "/api/v1/contents",
            "/api/v1/contents/**/detail",
            "/api/v1/contents/**/comments",
            "/api/v1/contents/**/comments/**",
            "/api/v1/members",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-ui/index.html"};

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("--------------------------- JwtVerifyFilter ---------------------------");
        String accessToken = jwtUtil.parseToken(request);
        JwtExceptionType validateResult = jwtUtil.validateToken(accessToken);

        if (validateResult == JwtExceptionType.VALID_JWT_TOKEN) {
            // 토큰에서 memberId와 role 획득
            Long memberId = jwtUtil.getMemberId(accessToken);
            RoleType role = jwtUtil.getRole(accessToken);

            Member jwtInfo = Member.builder()
                    .roleType(role)
                    .build();
            // UserDetails에 회원 정보 객체 담기
            CustomOAuth2User customOAuth2User = new CustomOAuth2User(jwtInfo);
            // 스프링 시큐리티 인증 토큰 생성
            Authentication authentication = new UsernamePasswordAuthenticationToken(memberId, null, customOAuth2User.getAuthorities());
            // 세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } else {
            ErrorStatus errorStatus;
            switch (validateResult) {
                case EMPTY_JWT: // 헤더에 토큰이 비어있음
                    errorStatus = ErrorStatus.EMPTY_JWT;
                    break;
                case EXPIRED_JWT_TOKEN: // 토큰 만료됨
                    errorStatus = ErrorStatus.EXPIRED_ACCESS;
                    break;
                case INVALID_JWT_TOKEN:
                    errorStatus = ErrorStatus.INVALID_TOKEN;
                    break;
                case UNSUPPORTED_JWT_TOKEN:
                    errorStatus = ErrorStatus.UNSUPPORTED_TOKEN;
                    break;
                case INVALID_JWT_SIGNATURE: // 다른 서비스의 토큰이 들어갔을때(서명이 다름)
                    errorStatus = ErrorStatus.INVALID_SIGNATURE;
                    break;
                default:
                    errorStatus = ErrorStatus.INTERNAL_SERVER_ERROR;
            }
            jwtAuthenticationEntryPoint.setResponse(response, errorStatus);
        }
    }
}