package com.server.InvestiMate.api.feedback.controller;

import com.server.InvestiMate.api.StockRecord.dto.StockRecordResponseDto;
import com.server.InvestiMate.api.feedback.dto.FeedbackDto;
import com.server.InvestiMate.api.feedback.service.FeedbackService;
import com.server.InvestiMate.common.response.ApiResponse;
import com.server.InvestiMate.common.response.SuccessStatus;
import com.server.InvestiMate.common.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    /**
     * 피드백 가능한 거래 기록 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<StockRecordResponseDto>>> getPossibleStockRecords(Principal principal) {
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(SuccessStatus.GET_TRADE_HISTORY_SUCCESS, feedbackService.getPossibleStockRecords(memberId));
    }

    /**
     * 피드백 저장
     */
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> saveFeedback(
            Principal principal,
            @RequestBody List<FeedbackDto> feedbackRequests,
            @PathVariable("id") Long stockRecordId
    ) {
        Long memberId = MemberUtil.getMemberId(principal);
        feedbackService.saveFeedback(memberId, feedbackRequests, stockRecordId);
        return ApiResponse.success(SuccessStatus.CREATE_FEEDBACK_SUCCESS);
    }


    /**
     * 피드백 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<List<FeedbackDto>>> getFeedback(
            Principal principal,
            @PathVariable("id") Long stockRecordId
    ) {
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(SuccessStatus.GET_FEEDBACK_SUCCESS, feedbackService.getFeedback(memberId, stockRecordId));
    }
}
