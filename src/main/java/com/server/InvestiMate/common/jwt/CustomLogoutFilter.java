package com.server.InvestiMate.common.jwt;

import com.server.InvestiMate.api.auth.service.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 *   로그아웃 메소드
 *   /login을 따로 Controller로 정의해줄 필요는 없다.
 *
 *   쿠키에서 리프레쉬 토큰을 꺼내 검증 후
 *   DB에서 리프레쉬 토큰 제거(null) 및 쿠키 값 또한 지움
 */
@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {
    private final JwtUtil jwtUtil;
    private final AuthService authService;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        this.doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }
    @Modifying
    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)  throws IOException, ServletException {
        // 요청 URI 중 ~/logout인 URI일 경우 해당 로그아웃 필터 로직을 수행함.
        String requestUri = request.getRequestURI();
        String requestMethod = request.getMethod();
        if (!requestUri.matches("^\\/logout$") || !requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        //get refresh token
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refreshToken = cookie.getValue();
            }
        }
        System.out.println("refreshToken = " + refreshToken);
        // 쿠키로 부터 가져온 리프레쉬 토큰 검증
        jwtUtil.validateToken(refreshToken);

        authService.validateRefreshAndLogout(refreshToken);

        //Refresh 토큰 Cookie 값 0
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
