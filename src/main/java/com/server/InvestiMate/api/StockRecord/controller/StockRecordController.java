package com.server.InvestiMate.api.StockRecord.controller;

import com.server.InvestiMate.api.StockRecord.dto.CreateStockRecordRequest;
import com.server.InvestiMate.api.StockRecord.dto.GetStockRecordResponse;
import com.server.InvestiMate.api.StockRecord.dto.StockRecordDto;
import com.server.InvestiMate.api.StockRecord.dto.StockRecordResponseDto;
import com.server.InvestiMate.api.StockRecord.service.StockRecordService;
import com.server.InvestiMate.common.response.ApiResponse;
import com.server.InvestiMate.common.response.SuccessStatus;
import com.server.InvestiMate.common.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stock-records")
public class StockRecordController {
    private final StockRecordService stockRecordService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createStockRecord(Principal principal, @RequestBody StockRecordDto stockRecordDto) {
        stockRecordService.createStockRecord(MemberUtil.getMemberId(principal), stockRecordDto);
        return ApiResponse.success(SuccessStatus.SAVE_TRADE_HISTORY_SUCCESS);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StockRecordResponseDto>>> getStockRecords(Principal principal) {
        Long memberId = MemberUtil.getMemberId(principal);
        List<StockRecordResponseDto> stockRecords = stockRecordService.getStockRecords(memberId);
        return ApiResponse.success(SuccessStatus.GET_TRADE_HISTORY_SUCCESS, stockRecords);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> updateStockRecord(
            Principal principal,
            @PathVariable("id") Long recordId,
            @RequestBody StockRecordDto stockRecordDto) {
        Long memberId = MemberUtil.getMemberId(principal);
        stockRecordService.updateStockRecord(memberId, recordId, stockRecordDto);
        return ApiResponse.success(SuccessStatus.UPDATE_TRADE_HISTORY_SUCCESS);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteStockRecord(Principal principal, @PathVariable("id") Long recordId) {
        Long memberId = MemberUtil.getMemberId(principal);
        stockRecordService.deleteStockRecord(memberId, recordId);
        return ApiResponse.success(SuccessStatus.DELETE_TRADE_HISTORY_SUCCESS);
    }
}
