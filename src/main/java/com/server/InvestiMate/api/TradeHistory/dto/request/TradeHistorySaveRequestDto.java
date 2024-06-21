package com.server.InvestiMate.api.TradeHistory.dto.request;

import com.server.InvestiMate.api.TradeHistory.domain.TradeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record TradeHistorySaveRequestDto (
        @NotBlank String stockCode,
        @NotBlank TradeType tradeType,
        @Positive int tradeAmount
){
}
