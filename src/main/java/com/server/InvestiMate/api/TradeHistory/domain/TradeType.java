package com.server.InvestiMate.api.TradeHistory.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TradeType {
    BUY("매수"), SELL("매도");
    private final String key;
}
