package com.server.InvestiMate.api.simulation.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.InvestiMate.api.simulation.dto.GetSimulationRequest;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

@Service
public class SimulationService {

    public List<Map<String, Object>> createSimulation(GetSimulationRequest getSimulationRequest) {
        try {
            // JSON 데이터로 변환
            ObjectMapper mapper = new ObjectMapper();
            String jsonData = mapper.writeValueAsString(getSimulationRequest);

            // Output the JSON data before passing it to the Python script
            System.out.println("JSON Data to send to Python: " + jsonData);

            // Python 스크립트 호출
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "python3",
                    "/app/feedback/feedback.py",
                    "get_simulation_result",  // 호출할 메서드 이름
                    jsonData  // JSON 데이터
            );
            Process process = processBuilder.start();

            // Output before reading the Python script's result
            System.out.println("Python script started, waiting for output...");

            // Python 스크립트의 JSON 반환 읽기
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
            }

            // Output after receiving the result
            System.out.println("Output from Python script: " + output.toString());

            // JSON 데이터를 List<Map<String, Object>>로 파싱
            List<Map<String, Object>> result = mapper.readValue(output.toString(), new TypeReference<List<Map<String, Object>>>() {});

            // Output the final result
            System.out.println("Parsed simulation result: " + result);

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
