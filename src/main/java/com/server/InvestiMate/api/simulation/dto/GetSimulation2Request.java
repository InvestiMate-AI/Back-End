package com.server.InvestiMate.api.simulation.dto;

public record GetSimulation2Request(
        String corp,          // 기업 이름
        long totalAsset,      // 총 자산
        double splitRate,     // 분할 비율
        String startDate,  // 시작 날짜
        String endDate,    // 종료 날짜
        String strategy       // 전략 이름
) {
}