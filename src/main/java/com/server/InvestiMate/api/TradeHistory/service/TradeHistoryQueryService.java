package com.server.InvestiMate.api.TradeHistory.service;

import com.server.InvestiMate.api.TradeHistory.domain.TradeHistory;
import com.server.InvestiMate.api.TradeHistory.dto.response.TradeHistoryGetResponse;
import com.server.InvestiMate.api.TradeHistory.repository.TradeHistoryRepository;
import com.server.InvestiMate.api.member.domain.Member;
import com.server.InvestiMate.api.member.repository.MemberRepository;
import com.server.InvestiMate.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TradeHistoryQueryService {
    private final TradeHistoryRepository tradeHistoryRepository;
    private final MemberRepository memberRepository;

    public List<TradeHistoryGetResponse> getTradeHistory(Long memberId, String stockCode) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        List<TradeHistory> tradeHistories = tradeHistoryRepository.findAllByMemberAndStockCodeOrderByCreatedDateDesc(member, stockCode);

        return tradeHistories.stream()
                .map(tradeHistory -> TradeHistoryGetResponse.of(tradeHistory))
                .collect(Collectors.toList());
    }
}
