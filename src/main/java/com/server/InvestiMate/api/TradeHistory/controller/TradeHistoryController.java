package com.server.InvestiMate.api.TradeHistory.controller;

import com.server.InvestiMate.api.TradeHistory.dto.request.TradeHistorySaveRequestDto;
import com.server.InvestiMate.api.TradeHistory.dto.response.TradeHistoryGetResponse;
import com.server.InvestiMate.api.TradeHistory.service.TradeHistoryCommandService;
import com.server.InvestiMate.api.TradeHistory.service.TradeHistoryQueryService;
import com.server.InvestiMate.common.response.ApiResponse;
import com.server.InvestiMate.common.response.SuccessStatus;
import com.server.InvestiMate.common.util.MemberUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/trade-histories")
@RequiredArgsConstructor
public class TradeHistoryController {
    private final TradeHistoryCommandService tradeHistoryCommandService;
    private final TradeHistoryQueryService tradeHistoryQueryService;

    /**
     * 거래 내역 저장
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> saveTradeHistory(Principal principal, @Valid @RequestBody TradeHistorySaveRequestDto tradeHistorySaveRequestDto) {
        tradeHistoryCommandService.saveTradeHistory(MemberUtil.getMemberId(principal),tradeHistorySaveRequestDto);
        return ApiResponse.success(SuccessStatus.SAVE_TRADE_HISTORY_SUCCESS);
    }

    /**
     * 나의 거래 내역 조회
     */
    @GetMapping("/{stockCode}")
    public ResponseEntity<ApiResponse<Object>> getTradeHistory(Principal principal, @PathVariable String stockCode) {
        List<TradeHistoryGetResponse> tradeHistory = tradeHistoryQueryService.getTradeHistory(MemberUtil.getMemberId(principal), stockCode);
        return ApiResponse.success(SuccessStatus.GET_COMMENT_ALL_SUCCESS, tradeHistory);
    }
}
