package com.server.InvestiMate.api.StockRecord.dto;

public record StockRecordResponseDto(
        Long stockRecordId,
        String date,
        String name,
        int volume,
        String type
) {
}
