package com.server.InvestiMate.api.simulation.controller;

import com.server.InvestiMate.api.simulation.dto.GetSimulation2Request;
import com.server.InvestiMate.api.simulation.dto.GetSimulationRequest;
import com.server.InvestiMate.api.simulation.service.SimulationService;
import com.server.InvestiMate.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import static com.server.InvestiMate.common.response.SuccessStatus.CREATE_SIMULATION2_SUCCESS;
import static com.server.InvestiMate.common.response.SuccessStatus.CREATE_SIMULATION_SUCCESS;

@RestController
@RequestMapping("/api/v1/simulations")
@RequiredArgsConstructor
public class SimulationController {
    private final SimulationService simulationService;

    @PostMapping
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> createSimulation(
            @RequestBody GetSimulationRequest getSimulationRequest
    ) {
        List<Map<String, Object>> simulationResult = simulationService.createSimulation(getSimulationRequest);
        if (simulationResult != null) {
            return ApiResponse.success(CREATE_SIMULATION_SUCCESS, simulationResult);
        } else {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/2")
    public ResponseEntity<ApiResponse<Object>> creSimulation2(
            @RequestBody GetSimulation2Request getSimulation2Request
    ) {
        List<Map<String, Object>> simulationResult = simulationService.createSimulation2(getSimulation2Request);
        if (simulationResult != null) {
            return ApiResponse.success(CREATE_SIMULATION2_SUCCESS, simulationResult);
        } else {
            return ResponseEntity.status(500).build();
        }
    }


}
