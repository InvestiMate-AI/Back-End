package com.server.InvestiMate.api.member.controller;

import com.server.InvestiMate.api.member.dto.request.MemberSaveProfileDto;
import com.server.InvestiMate.api.member.dto.response.MemberGetProfileResponseDto;
import com.server.InvestiMate.api.member.service.MemberService;
import com.server.InvestiMate.common.response.ApiResponse;
import com.server.InvestiMate.common.response.SuccessStatus;
import com.server.InvestiMate.common.util.MemberUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 특정 Member 정보 조회
    @GetMapping
    public ResponseEntity<ApiResponse<MemberGetProfileResponseDto>> getMemberProfile(Principal principal) {
        return ApiResponse.success(SuccessStatus.GET_PROFILE_SUCCESS, memberService.getMemberProfile(MemberUtil.getMemberOAuth2Id(principal)));
    }

    // Member 유저 프로필 업데이트
    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<Object>> saveMemberProfile(Principal principal, @Valid @RequestBody MemberSaveProfileDto memberSaveProfileDto) {
        String memberOAuth2Id = MemberUtil.getMemberOAuth2Id(principal);
        memberService.saveMemberProfile(memberOAuth2Id, memberSaveProfileDto);
        return ApiResponse.success(SuccessStatus.SAVE_MEMBER_PROFILE);
    }
}
