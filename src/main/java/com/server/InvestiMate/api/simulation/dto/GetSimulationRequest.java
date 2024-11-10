package com.server.InvestiMate.api.simulation.dto;

import java.util.List;
public record GetSimulationRequest(
        String corp,
        long totalAsset,
        double splitRate,
        String startDate,
        String endDate,
        List<List<String>> buyOption,
        List<List<String>> sellOption
) {}