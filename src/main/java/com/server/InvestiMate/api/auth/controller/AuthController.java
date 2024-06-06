package com.server.InvestiMate.api.auth.controller;

import com.server.InvestiMate.api.auth.dto.request.AuthRequestDto;
import com.server.InvestiMate.api.auth.dto.response.AuthTokenResponseDto;

import com.server.InvestiMate.api.auth.service.AuthService;
import com.server.InvestiMate.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.server.InvestiMate.common.response.SuccessStatus.GET_NEW_TOKEN_SUCCESS;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Reissuing Access Token When Access Token is Expired.
     */
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<AuthTokenResponseDto>> getNewAccessToken(@Valid @RequestBody AuthRequestDto authRequestDto) {
        AuthTokenResponseDto newToken = authService.getNewToken(authRequestDto.refreshToken());
        return ApiResponse.success(GET_NEW_TOKEN_SUCCESS, newToken);
    }
}
