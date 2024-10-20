package com.server.InvestiMate.api.StockRecord.dto;

import java.util.List;

public record CreateStockRecordRequest(
        List<StockRecordDto> data
) {
}
