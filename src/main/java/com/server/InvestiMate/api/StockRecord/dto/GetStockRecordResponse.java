package com.server.InvestiMate.api.StockRecord.dto;

import java.util.List;

public record GetStockRecordResponse(
        List<StockRecordDto> stockRecords
) {
}
