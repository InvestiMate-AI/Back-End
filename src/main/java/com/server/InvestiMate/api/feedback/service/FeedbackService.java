package com.server.InvestiMate.api.feedback.service;

import com.server.InvestiMate.api.StockRecord.domain.StockRecord;
import com.server.InvestiMate.api.StockRecord.dto.StockRecordResponseDto;
import com.server.InvestiMate.api.StockRecord.repository.StockRecordRepository;
import com.server.InvestiMate.api.feedback.domain.Feedback;
import com.server.InvestiMate.api.feedback.dto.FeedbackDto;
import com.server.InvestiMate.api.feedback.repository.FeedbackRepository;
import com.server.InvestiMate.api.member.domain.Member;
import com.server.InvestiMate.api.member.repository.MemberRepository;
import com.server.InvestiMate.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final StockRecordRepository stockRecordRepository;
    private final MemberRepository memberRepository;
    private final FeedbackRepository feedbackRepository;
    @Transactional(readOnly = true)
    public List<StockRecordResponseDto> getPossibleStockRecords(Long memberId) {
        return stockRecordRepository.findByMemberIdAndHasFeedbackFalse(memberId).stream()
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
    public void saveFeedback(Long memberId, List<FeedbackDto> feedbackDtos, Long stockRecordId) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);

        StockRecord stockRecord = stockRecordRepository.findById(stockRecordId)
                .orElseThrow(() -> new NotFoundException("Stock record not found for this user"));

        for (FeedbackDto request : feedbackDtos) {
            Feedback feedback = Feedback.builder()
                    .feedbackIndex(request.index())
                    .feedbackType(request.type())
                    .data(request.data())
                    .member(member)
                    .stockRecord(stockRecord)
                    .build();

            feedbackRepository.save(feedback);
        }

        stockRecord.setHasFeedback(true);
    }

    @Transactional(readOnly = true)
    public List<FeedbackDto> getFeedback(Long memberId, Long stockRecordId) {
        List<Feedback> feedbacks = feedbackRepository.findByMemberIdAndStockRecordId(memberId, stockRecordId);

        return feedbacks.stream()
                .map(feedback -> new FeedbackDto(
                        feedback.getIndex(),
                        feedback.getFeedbackType(),
                        feedback.getData()))
                .collect(Collectors.toList());
    }
}
