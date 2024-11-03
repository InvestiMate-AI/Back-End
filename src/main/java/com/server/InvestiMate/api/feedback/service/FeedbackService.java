package com.server.InvestiMate.api.feedback.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.InvestiMate.api.StockRecord.domain.StockRecord;
import com.server.InvestiMate.api.StockRecord.dto.StockRecordInputDto;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Transactional(readOnly = true)
    public List<StockRecordResponseDto> getImpossibleStockRecords(Long memberId) {
        return stockRecordRepository.findByMemberIdAndHasFeedbackTrue(memberId).stream()
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

    @Transactional
    public void saveFeedbackV2(Long memberId, Long stockRecordId) {
        Member member = memberRepository.findMemberByIdOrThrow(memberId);

        StockRecord stockRecord = stockRecordRepository.findById(stockRecordId)
                .orElseThrow(() -> new NotFoundException("Stock record not found for this user"));
        System.out.println("stockRecord.getStockName()+ stockRecord.getTradeDate() + stockRecord.getTradeType() = " + stockRecord.getStockName()+ stockRecord.getTradeDate() + stockRecord.getTradeType());
        try {
            // ProcessBuilder로 Python 스크립트 호출
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "python3",
                    "/app/feedback/feedback.py", // 경로 지정
                    "get_feedback", // 메소드명 추가
                    stockRecord.getStockName(), // 예시: 종목명 인자
                    stockRecord.getTradeDate(), // 예시: 날짜 인자
                    stockRecord.getTradeType() // 예시: 매수 또는 매도 인자
            );
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Python 스크립트 출력 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            // 프로세스 종료 코드 확인
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Python script execution failed with code: " + exitCode);
            }


// Python 스크립트의 출력 결과를 콘솔에 출력
            System.out.println("Python script output: " + output.toString());
            ObjectMapper mapper = new ObjectMapper();
            List<FeedbackDto> feedbackDtos = mapper.readValue(output.toString(),
                    mapper.getTypeFactory().constructCollectionType(List.class, FeedbackDto.class));

            // FeedbackDto 리스트를 Feedback 엔티티로 변환하여 저장
            for (FeedbackDto request : feedbackDtos) {
                Feedback feedback = Feedback.builder()
                        .feedbackIndex(request.index()) // getIndex() 메서드 사용
                        .feedbackType(request.type()) // getType() 메서드 사용
                        .data(request.data()) // getData() 메서드 사용
                        .member(member)
                        .stockRecord(stockRecord)
                        .build();

                feedbackRepository.save(feedback);
            }

            // 피드백이 저장된 후 주식 기록에 피드백이 추가됨을 표시
            stockRecord.setHasFeedback(true);

        } catch (Exception e) {
            e.printStackTrace();
            // Optional: Handle the error more gracefully or log it
        }
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
