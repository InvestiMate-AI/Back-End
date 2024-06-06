package com.server.InvestiMate.api.chat.dto.request;

public record MessageCreateRequestDto(
        String question,
        String answer
) {}
