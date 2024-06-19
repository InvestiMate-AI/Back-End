package com.server.InvestiMate.api.chat.dto.response;

import com.server.InvestiMate.api.chat.domain.Thread;

public record ThreadGetAllResponseDto(
        Long chatRoomId,
        String assistantId,
        String threadId,
        Integer reportYear,
        String reportCompany,
        String reportType

) {
    public static ThreadGetAllResponseDto of(Thread thread){
        return new ThreadGetAllResponseDto(
                thread.getId(),
                thread.getReport().getAssistantsId(),
                thread.getThreadId(),
                thread.getReport().getReportYear(),
                thread.getReport().getReportCompany(),
                thread.getReport().getReportType()
        );
    }
}