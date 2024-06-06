package com.server.InvestiMate.api.chat.domain;

import com.server.InvestiMate.api.member.domain.Member;
import com.server.InvestiMate.common.auditing.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 특정한 채팅방에서 모든 질문 및 응답 내역들 기록
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id")
    private Thread thread;

    @Column(length = 1000)
    private String question;

    @Column(length = 1000)
    private String answer;

    @Builder
    public Message(Member member, Thread thread, String question, String answer) {
        this.member = member;
        this.thread = thread;
        this.question = question;
        this.answer = answer;
    }
}