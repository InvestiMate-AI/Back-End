package com.server.InvestiMate.api.TradeHistory.dto.response;

import com.server.InvestiMate.api.TradeHistory.domain.TradeHistory;
import com.server.InvestiMate.api.TradeHistory.domain.TradeType;
import com.server.InvestiMate.common.util.TimeUtil;

public record TradeHistoryGetResponse(
        String stockCode,
        TradeType tradeType,
        int tradeAmount,
        int holdingAmount,
        String tradeDate,
        String nickname

) {
    public static TradeHistoryGetResponse of(TradeHistory tradeHistory) {
        return new TradeHistoryGetResponse(
            tradeHistory.getStockCode(),
            tradeHistory.getTradeType(),
            tradeHistory.getTradeAmount(),
            tradeHistory.getHoldingAmount(),
            TimeUtil.refineTime(tradeHistory.getCreatedDate()),
            tradeHistory.getMember().getNickname()
        );
    }

}
