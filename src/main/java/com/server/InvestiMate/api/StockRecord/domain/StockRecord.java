package com.server.InvestiMate.api.StockRecord.domain;

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
public class StockRecord extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "trade_date")
    private String tradeDate;

    @Column(nullable = false, name = "stock_name")
    private String stockName;

    @Column(nullable = false, name = "trade_volume")
    private int tradeVolume;

    @Column(nullable = false, name = "trade_type")
    private String tradeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private boolean hasFeedback = false;

    @Builder
    public StockRecord(Long id, String tradeDate, String stockName, int tradeVolume, String tradeType, Member member) {
        this.id = id;
        this.tradeDate = tradeDate;
        this.stockName = stockName;
        this.tradeVolume = tradeVolume;
        this.tradeType = tradeType;
        this.member = member;
    }

    public void setStockRecord(String date, String name, int volume, String type) {
        this.tradeDate = date;
        this.stockName = name;
        this.tradeVolume = volume;
        this.tradeType = type;
    }

    public void setHasFeedback(boolean hasFeedback) {
        this.hasFeedback = hasFeedback;
    }
}
