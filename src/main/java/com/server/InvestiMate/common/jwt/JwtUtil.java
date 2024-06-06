package com.server.InvestiMate.common.jwt;

import com.server.InvestiMate.api.member.domain.RoleType;
import com.server.InvestiMate.common.jwt.exception.JwtExceptionType;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

/**
 * https://github.com/jwtk/jjwt
 */
@Slf4j
@Component
public class JwtUtil {
    @Value("${spring.jwt.expire-length.access-expire}")
    public Long accessTokenExpireLength;
    @Value("${spring.jwt.expire-length.refresh-expire}")
    protected Long refreshTokenExpireLength;
    @Value("${spring.jwt.redirect-url}")
    protected String JWT_REDIRECT;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private SecretKey secretKey;
    // UTF-8로 인코딩된 비밀키를 바이트 배열로 변환하고, HS256 알고리즘으로 JWT 서명에 사용할 SecretKey를 생성
    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }
    /**
     * 토큰 생성
     */
    public String generateToken(String category, String oAuth2Id, String role, Long expiredMs) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expiredMs * 1000);

        return Jwts.builder()
                .claim("category", category)
                .claim("oAuth2Id", oAuth2Id)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }


    public String parseToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);

        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        } else {
            return header.split(" ")[1];
        }
    }
    public String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    // 토큰 유효성 검증
    public JwtExceptionType validateToken(String token) {
        try {
            // JWT 파서를 생성하고, parseClaimsJws(token) 메소드로 토큰의 유효성을 검증
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
            return JwtExceptionType.VALID_JWT_TOKEN;
        } catch (io.jsonwebtoken.security.SignatureException exception) {
            log.error("잘못된 JWT 서명을 가진 토큰입니다.");
            return JwtExceptionType.INVALID_JWT_SIGNATURE;
        } catch (MalformedJwtException exception) {
            log.error("잘못된 JWT 토큰입니다.");
            return JwtExceptionType.INVALID_JWT_TOKEN;
        } catch (ExpiredJwtException exception) {
            log.error("만료된 JWT 토큰입니다.");
            return JwtExceptionType.EXPIRED_JWT_TOKEN;
        } catch (UnsupportedJwtException exception) {
            log.error("지원하지 않는 JWT 토큰입니다.");
            return JwtExceptionType.UNSUPPORTED_JWT_TOKEN;
        } catch (IllegalArgumentException exception) {
            log.error("JWT Claims가 비어있습니다.");
            return JwtExceptionType.EMPTY_JWT;
        }
    }

    public String getOAuth2Id(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("oAuth2Id", String.class);
    }

    public RoleType getRole(String token) {
        String roleString = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
        // 열거형에 해당하는 문자열을 찾아 반환하거나 기본값으로 USER를 반환합니다.
        return Arrays.stream(RoleType.values())
                .filter(roleType -> roleType.getKey().equals(roleString))
                .findFirst()
                .orElse(RoleType.USER);
    }

}
