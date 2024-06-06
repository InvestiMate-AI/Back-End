package com.server.InvestiMate.api.chat.domain;

import com.server.InvestiMate.api.member.domain.Member;
import com.server.InvestiMate.common.auditing.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 채팅방 하나의 역할을 함
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Thread extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") //댓글 작성자 id
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assistant_id") // 외래키를 assistant_id로 지정
    private Report report;

    @Column(name = "thread_id")
    private String threadId;

    @OneToMany(mappedBy = "thread")
    private List<Message> messages = new ArrayList<>();

    @Builder
    public Thread(Member member, Report report, String threadId) {
        this.member = member;
        this.report = report;
        this.threadId = threadId;
    }
}
