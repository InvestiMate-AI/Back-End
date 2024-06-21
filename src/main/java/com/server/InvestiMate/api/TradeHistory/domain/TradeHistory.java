package com.server.InvestiMate.api.TradeHistory.domain;

import com.server.InvestiMate.api.member.domain.Member;
import com.server.InvestiMate.common.auditing.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TradeHistory extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "stock_code")
    private String stockCode;

    @Column(nullable = false, name = "trade_type")
    private TradeType tradeType;

    @Column(nullable = false, name = "trade_amount")
    private int tradeAmount;

    @Column(nullable = false, name = "holding_amount")
    private int holdingAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    private TradeHistory(String stockCode, TradeType tradeType, int tradeAmount, int holdingAmount, Member member) {
        this.stockCode = stockCode;
        this.tradeType = tradeType;
        this.tradeAmount = tradeAmount;
        this.holdingAmount = holdingAmount;
        this.member = member;
    }
}
