package com.server.InvestiMate.api.feedback.repository;

import com.server.InvestiMate.api.StockRecord.domain.StockRecord;
import com.server.InvestiMate.api.feedback.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByMemberIdAndStockRecordId(Long memberId, Long stockRecordId);
}
