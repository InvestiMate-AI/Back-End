package com.server.InvestiMate.api.chat.repository;

import com.server.InvestiMate.api.chat.domain.Thread;
import com.server.InvestiMate.common.exception.NotFoundException;
import com.server.InvestiMate.common.response.ErrorStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ThreadRepository extends JpaRepository<Thread, Long> {

    Optional<Thread> findThreadById(Long chatRoomId);

    default Thread findThreadByIdOrThrow(Long chatRoomId) {
        return findThreadById(chatRoomId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_MEMBER.getMessage()));
    }

}
