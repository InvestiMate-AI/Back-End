package com.server.InvestiMate.api.TradeHistory.dto.request;

import com.server.InvestiMate.api.TradeHistory.domain.TradeType;
import jakarta.validation.constraints.NotBlank;

public record TradeHistorySaveRequestDto (
        @NotBlank String stockCode,
        @NotBlank TradeType tradeType,
        @NotBlank int tradeAmount
){
}
