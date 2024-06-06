package com.server.InvestiMate.api.chat.repository;

import com.server.InvestiMate.api.chat.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllById(Long chatRoomId);
}
