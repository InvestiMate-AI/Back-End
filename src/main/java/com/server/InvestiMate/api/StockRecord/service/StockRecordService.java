package com.server.InvestiMate.api.StockRecord.service;

import com.server.InvestiMate.api.StockRecord.domain.StockRecord;
import com.server.InvestiMate.api.StockRecord.dto.StockRecordDto;
import com.server.InvestiMate.api.StockRecord.dto.StockRecordResponseDto;
import com.server.InvestiMate.api.StockRecord.repository.StockRecordRepository;
import com.server.InvestiMate.api.member.domain.Member;
import com.server.InvestiMate.api.member.repository.MemberRepository;
import com.server.InvestiMate.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockRecordService {
    private final StockRecordRepository stockRecordRepository;
    private final MemberRepository memberRepository;
    @Transactional
    public void createStockRecord(Long memberId, StockRecordDto stockRecordDto) {
        log.warn(stockRecordDto.name());
        Member member = memberRepository.findMemberByIdOrThrow(memberId);
        StockRecord stockRecord = StockRecord.builder()
                .tradeDate(stockRecordDto.date())
                .stockName(stockRecordDto.name())
                .tradeVolume(stockRecordDto.volume())
                .tradeType(stockRecordDto.type())
                .member(member)
                .build();
        stockRecordRepository.save(stockRecord);
    }

    @Transactional(readOnly = true)
    public List<StockRecordResponseDto> getStockRecords(Long memberId) {
        return stockRecordRepository.findByMemberId(memberId).stream()
                .map(record -> new StockRecordResponseDto(
                        record.getId(),
                        record.getTradeDate(),
                        record.getStockName(),
                        record.getTradeVolume(),
                        record.getTradeType()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateStockRecord(Long memberId, Long recordId, StockRecordDto stockRecordDto) {
        StockRecord stockRecord = stockRecordRepository.findByIdAndMemberId(recordId, memberId)
                .orElseThrow(() -> new NotFoundException("Stock record not found for this user"));

        stockRecord.setStockRecord(stockRecordDto.date(), stockRecordDto.name(), stockRecordDto.volume(), stockRecordDto.type());
    }

    @Transactional
    public void deleteStockRecord(Long memberId, Long recordId) {
        StockRecord stockRecord = stockRecordRepository.findByIdAndMemberId(recordId, memberId)
                .orElseThrow(() -> new NotFoundException("Stock record not found for this user"));

        stockRecordRepository.delete(stockRecord);
    }
}
