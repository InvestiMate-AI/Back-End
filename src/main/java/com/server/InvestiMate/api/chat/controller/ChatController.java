package com.server.InvestiMate.api.chat.controller;

import com.server.InvestiMate.api.chat.dto.request.ChatCreateThreadDto;
import com.server.InvestiMate.api.chat.service.ChatService;
import com.server.InvestiMate.common.response.ApiResponse;
import com.server.InvestiMate.common.response.SuccessStatus;
import com.server.InvestiMate.common.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    /**
     * 하나의 채팅방으로 보면 됨
     */
    @PostMapping("/thread")
    public ResponseEntity<ApiResponse<Object>> createThread(Principal principal, @RequestBody ChatCreateThreadDto chatSaveAssistantDto) {
        String memberOAuth2Id = MemberUtil.getMemberOAuth2Id(principal);
        chatService.createTread(memberOAuth2Id, chatSaveAssistantDto);
        return ApiResponse.success(SuccessStatus.CREATE_THREAD_SUCCESS);
    }
}
