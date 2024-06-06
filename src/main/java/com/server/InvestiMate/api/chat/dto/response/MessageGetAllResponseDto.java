package com.server.InvestiMate.api.chat.dto.response;

import com.server.InvestiMate.api.chat.domain.Message;
import com.server.InvestiMate.common.util.TimeUtil;

public record MessageGetAllResponseDto(
        String time,
        String question,
        String answer
){
    public static MessageGetAllResponseDto of(Message message){
        return new MessageGetAllResponseDto(
                TimeUtil.refineTime(message.getCreatedDate()),
                message.getQuestion(),
                message.getAnswer()
        );
    }
}
