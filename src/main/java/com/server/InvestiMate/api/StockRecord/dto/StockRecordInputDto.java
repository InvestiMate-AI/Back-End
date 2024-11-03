package com.server.InvestiMate.api.StockRecord.dto;

public record StockRecordInputDto(
        String stockName,
        String stockDate,
        String stockType
) {
}
