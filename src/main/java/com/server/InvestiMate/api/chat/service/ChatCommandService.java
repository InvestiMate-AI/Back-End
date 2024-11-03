package com.server.InvestiMate.api.chat.service;

import com.server.InvestiMate.api.StockRecord.domain.StockRecord;
import com.server.InvestiMate.api.chat.domain.Message;
import com.server.InvestiMate.api.chat.domain.Thread;
import com.server.InvestiMate.api.chat.domain.Report;
import com.server.InvestiMate.api.chat.dto.request.ChatCreateThreadDto;
import com.server.InvestiMate.api.chat.dto.request.MessageCreateRequestDto;
import com.server.InvestiMate.api.chat.dto.response.ThreadsResponseDto;
import com.server.InvestiMate.api.chat.repository.MessageRepository;
import com.server.InvestiMate.api.chat.repository.ThreadRepository;
import com.server.InvestiMate.api.chat.repository.ReportRepository;
import com.server.InvestiMate.api.member.domain.Member;
import com.server.InvestiMate.api.member.repository.MemberRepository;
import com.server.InvestiMate.common.client.openai.AssistantsClient;
import com.server.InvestiMate.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatCommandService {
    private final AssistantsClient assistantsClient;
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;
    private final ThreadRepository threadRepository;
    private final MessageRepository messageRepository;


    public void createThread(Long memberId, ChatCreateThreadDto chatSaveAssistantDto) {
        Integer year = Integer.valueOf(chatSaveAssistantDto.year());
        String companyName = chatSaveAssistantDto.companyName();
        String reportType = chatSaveAssistantDto.reportType();
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        Report report = reportRepository.findByReportYearAndReportCompanyAndReportTypeOrThrow(year, companyName, reportType);

        // Implement this method to generate a unique assistant ID
        ThreadsResponseDto threads = assistantsClient.createThreads();
        Thread thread = Thread.builder()
                .member(member)
                .report(report)
                .threadId(threads.id())
                .build();
        threadRepository.save(thread);
    }

    public void saveMessage(Long memberId, Long chatRoomId, MessageCreateRequestDto messageCreateRequestDto) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        Thread thread = threadRepository.findThreadByIdOrThrow(chatRoomId);
        Message message = Message.builder()
                .member(member)
                .thread(thread)
                .question(messageCreateRequestDto.question())
                .answer(messageCreateRequestDto.answer())
                .build();
        messageRepository.save(message);
    }

    public void deleteThread(Long memberId, Long chatId) {

        Thread thread = threadRepository.findByIdAndMemberId(chatId, memberId)
                .orElseThrow(() -> new NotFoundException("Thread not found for this user"));
        threadRepository.delete(thread);
    }
}
