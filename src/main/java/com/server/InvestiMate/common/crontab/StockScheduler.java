package com.server.InvestiMate.common.crontab;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class StockScheduler {

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    @Transactional
    public void updateStockDb() {
        try {
            // Python 스크립트 실행
            ProcessBuilder processBuilder = new ProcessBuilder("python", "path_to_your_python_script.py");
            Process process = processBuilder.start();

            // 프로세스 실행 결과 가져오기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Python 스크립트에서 출력한 내용을 처리
            }

            int exitCode = process.waitFor();
            System.out.println("Python script exited with code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
