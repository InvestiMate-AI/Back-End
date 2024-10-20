package com.server.InvestiMate.api.StockRecord.repository;

import com.server.InvestiMate.api.StockRecord.domain.StockRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRecordRepository extends JpaRepository<StockRecord, Long> {
    List<StockRecord> findByMemberId(Long memberId);
    Optional<StockRecord> findById(Long id);
    Optional<StockRecord> findByIdAndMemberId(Long id, Long memberId);
    List<StockRecord> findByMemberIdAndHasFeedbackFalse(Long memberId);
}
