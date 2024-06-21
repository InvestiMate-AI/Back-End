package com.server.InvestiMate.api.TradeHistory.service;

import com.server.InvestiMate.api.TradeHistory.domain.TradeHistory;
import com.server.InvestiMate.api.TradeHistory.domain.TradeType;
import com.server.InvestiMate.api.TradeHistory.dto.request.TradeHistorySaveRequestDto;
import com.server.InvestiMate.api.TradeHistory.repository.TradeHistoryRepository;
import com.server.InvestiMate.api.member.domain.Member;
import com.server.InvestiMate.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TradeHistoryCommandService {
    private final TradeHistoryRepository tradeHistoryRepository;
    private final MemberRepository memberRepository;
    public void saveTradeHistory(Long memberId, TradeHistorySaveRequestDto tradeHistorySaveRequestDto){
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        String stockCode = tradeHistorySaveRequestDto.stockCode();
        int tradeAmount = tradeHistorySaveRequestDto.tradeAmount();
        TradeType tradeType = tradeHistorySaveRequestDto.tradeType();
        Optional<TradeHistory> latestTradeHistory = tradeHistoryRepository.findFirstByMemberAndStockCodeOrderByCreatedDateDesc(member, stockCode);
        int holdingAmount = latestTradeHistory.map(TradeHistory::getHoldingAmount).orElse(0);

        if (tradeType == TradeType.BUY) {
            holdingAmount += tradeAmount;
        } else {
            holdingAmount -= tradeAmount;
        }

        TradeHistory tradeHistory = TradeHistory.builder()
                .stockCode(stockCode)
                .tradeType(tradeType)
                .tradeAmount(tradeAmount)
                .holdingAmount(holdingAmount)
                .member(member)
                .build();
        tradeHistoryRepository.save(tradeHistory);
    }
}
