package com.server.InvestiMate.api.StockRecord.dto;

import java.util.List;

public record StockRecordDto(
        String date,
        String name,
        int volume,
        String type
) {
}
