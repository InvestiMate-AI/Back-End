package com.server.InvestiMate.api.chat.controller;

import com.server.InvestiMate.api.chat.dto.request.ChatCreateThreadDto;
import com.server.InvestiMate.api.chat.dto.request.MessageCreateRequestDto;
import com.server.InvestiMate.api.chat.dto.response.MessageGetAllResponseDto;
import com.server.InvestiMate.api.chat.dto.response.ThreadGetAllResponseDto;
import com.server.InvestiMate.api.chat.service.ChatCommandService;
import com.server.InvestiMate.api.chat.service.ChatQueryService;
import com.server.InvestiMate.common.response.ApiResponse;
import com.server.InvestiMate.common.response.SuccessStatus;
import com.server.InvestiMate.common.util.MemberUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatCommandService chatCommandService;
    private final ChatQueryService chatQueryService;

    /**
     * Post
     * Thread 생성
     * 하나의 채팅방으로 보면 됨
     */
    @PostMapping("/thread")
    public ResponseEntity<ApiResponse<Object>> createThread(Principal principal, @Valid @RequestBody ChatCreateThreadDto chatSaveAssistantDto) {
        Long memberId = MemberUtil.getMemberId(principal);
        chatCommandService.createThread(memberId, chatSaveAssistantDto);
        return ApiResponse.success(SuccessStatus.CREATE_THREAD_SUCCESS);
    }

    /**
     * GET
     * 특정 유저의 채팅방 리스트
     * 즉, thread_id 반환
     * 이때, 해당 스레드의 어시스턴트의 리포트 정보도 같이 반환
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getThreads(Principal principal) {
        Long memberId = MemberUtil.getMemberId(principal);
        List<ThreadGetAllResponseDto> threads = chatQueryService.getThreads(memberId);
        return ApiResponse.success(SuccessStatus.GET_THREADS_SUCCESS, threads);
    }

    /**
     * GET RequestDTO: thread_id
     * 특정 채팅방 선택 시 채팅 기록 반환
     */
    @GetMapping("{chatRoomId}")
    public  ResponseEntity<ApiResponse<Object>> getChatRoom(Principal principal, @PathVariable Long chatRoomId) {
        List<MessageGetAllResponseDto> chatRoom = chatQueryService.getChatRoom(chatRoomId);
        return ApiResponse.success(SuccessStatus.GET_THREADS_SUCCESS, chatRoom);
    }


    /**
     * POST RequestDTO: 날짜, 메세지 내용, jwt토큰(헤더)
     * 응답 메세지 저장
     */
    @PostMapping("/{chatRoomId}")
    public ResponseEntity<ApiResponse<Object>> saveMessage(
            Principal principal,
            @PathVariable Long chatRoomId,
            @RequestBody MessageCreateRequestDto messageCreateRequestDto )
    {
        chatCommandService.saveMessage(MemberUtil.getMemberId(principal), chatRoomId, messageCreateRequestDto);
        return ApiResponse.success(SuccessStatus.CREATE_MESSAGE_SUCCESS);
    }
}
