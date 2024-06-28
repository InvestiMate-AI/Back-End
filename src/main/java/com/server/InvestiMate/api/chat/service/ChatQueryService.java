package com.server.InvestiMate.api.chat.service;

import com.server.InvestiMate.api.chat.domain.Message;
import com.server.InvestiMate.api.chat.domain.Thread;
import com.server.InvestiMate.api.chat.dto.response.MessageGetAllResponseDto;
import com.server.InvestiMate.api.chat.dto.response.ThreadGetAllResponseDto;
import com.server.InvestiMate.api.chat.repository.MessageRepository;
import com.server.InvestiMate.api.chat.repository.ThreadRepository;
import com.server.InvestiMate.api.member.domain.Member;
import com.server.InvestiMate.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatQueryService {
    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;
    private final ThreadRepository threadRepository;
    public List<ThreadGetAllResponseDto> getThreads(Long memberId) {
        List<ThreadGetAllResponseDto> threads = memberRepository.findThreadsByMemberId(memberId);
        return threads;
    }

    public List<MessageGetAllResponseDto> getChatRoom(Long chatRoomId) {
        List<Message> messages = threadRepository.findThreadByIdOrThrow(chatRoomId).getMessages();
        return messages.stream()
                .map(chatMessage -> MessageGetAllResponseDto.of(chatMessage))
                .collect(Collectors.toList());
    }
}
