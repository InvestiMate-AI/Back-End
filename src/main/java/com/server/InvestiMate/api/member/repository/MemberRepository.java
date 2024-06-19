package com.server.InvestiMate.api.member.repository;


import com.server.InvestiMate.api.chat.dto.response.ThreadGetAllResponseDto;
import com.server.InvestiMate.api.member.domain.Member;
import com.server.InvestiMate.common.exception.NotFoundException;
import com.server.InvestiMate.common.response.ErrorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberById(Long memberId);
    default Member findMemberByIdOrThrow(Long memberId) {
        return findMemberById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_MEMBER.getMessage()));
    }
    Optional<Member> findByoAuth2Id(String oAuth2Id);

    Optional<Member> findMemberByRefreshToken(String refreshToken);
    default Member findMemberByRefreshTokenOrThrow(String refreshToken) {
        return findMemberByRefreshToken(refreshToken)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_MEMBER.getMessage()));
    }

    // JPQL 한방 쿼리 DTO바로 반환
    @Query("SELECT new com.server.InvestiMate.api.chat.dto.response.ThreadGetAllResponseDto(" +
            "t.id, r.assistantsId, t.threadId, r.reportYear, r.reportCompany, r.reportType) " +
            "FROM Thread t " +
            "JOIN t.report r " +
            "JOIN t.member m " +
            "WHERE m.id = :memberId")
    List<ThreadGetAllResponseDto> findThreadsByMemberId(@Param("memberId") Long memberId);

}
