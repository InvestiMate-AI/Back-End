package com.server.InvestiMate.api.TradeHistory.repository;

import com.server.InvestiMate.api.TradeHistory.domain.TradeHistory;
import com.server.InvestiMate.api.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TradeHistoryRepository extends JpaRepository<TradeHistory, Long> {
    List<TradeHistory> findAllByMemberAndStockCodeOrderByCreatedDateDesc(Member member, String stockCode);

    Optional<TradeHistory> findFirstByMemberAndStockCodeOrderByCreatedDateDesc(Member member, String stockCode);

}
