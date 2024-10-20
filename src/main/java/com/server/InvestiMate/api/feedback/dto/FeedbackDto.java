package com.server.InvestiMate.api.feedback.dto;

public record FeedbackDto(
        Long index,
        String type,
        String data
) {}