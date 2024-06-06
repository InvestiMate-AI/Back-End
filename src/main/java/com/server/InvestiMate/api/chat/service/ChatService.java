package com.server.InvestiMate.api.chat.service;

import com.server.InvestiMate.api.chat.domain.ChatSession;
import com.server.InvestiMate.api.chat.domain.Report;
import com.server.InvestiMate.api.chat.dto.request.ChatCreateThreadDto;
import com.server.InvestiMate.api.chat.dto.response.ThreadsResponseDto;
import com.server.InvestiMate.api.chat.repository.ChatSessionRepository;
import com.server.InvestiMate.api.chat.repository.ReportRepository;
import com.server.InvestiMate.api.member.domain.Member;
import com.server.InvestiMate.api.member.repository.MemberRepository;
import com.server.InvestiMate.common.client.openai.AssistantsClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {
    private final AssistantsClient assistantsClient;
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;
    private final ChatSessionRepository chatSessionRepository;


    public void createThread(String memberOAuth2Id, ChatCreateThreadDto chatSaveAssistantDto) {
        Integer year = Integer.valueOf(chatSaveAssistantDto.year());
        String companyName = chatSaveAssistantDto.companyName();
        String reportType = chatSaveAssistantDto.reportType();
        Member member = memberRepository.findByOAuth2IdOrThrow(memberOAuth2Id);
        Report report = reportRepository.findByReportYearAndReportCompanyAndReportTypeOrThrow(year, companyName, reportType);

        // Implement this method to generate a unique assistant ID
        ThreadsResponseDto threads = assistantsClient.createThreads();
        ChatSession chatSession = ChatSession.builder()
                .member(member)
                .report(report)
                .threadId(threads.id())
                .build();
        chatSessionRepository.save(chatSession);
    }

}
