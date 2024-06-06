package com.server.InvestiMate.api.chat.dto.response;

import com.server.InvestiMate.api.chat.domain.Thread;

public record ThreadGetAllResponseDto(
        Long chatRoomId,
        String threadId,
        Integer reportYear,
        String reportCompany,
        String reportType

) {
    public static ThreadGetAllResponseDto of(Thread thread){
        return new ThreadGetAllResponseDto(
                thread.getId(),
                thread.getThreadId(),
                thread.getReport().getReportYear(),
                thread.getReport().getReportCompany(),
                thread.getReport().getReportType()
        );
    }
}