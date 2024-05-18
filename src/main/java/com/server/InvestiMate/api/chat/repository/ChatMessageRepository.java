package com.server.InvestiMate.api.chat.repository;

import com.server.InvestiMate.api.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
