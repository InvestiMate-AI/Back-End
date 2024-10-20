package com.server.InvestiMate.api.feedback.domain;

import com.server.InvestiMate.api.StockRecord.domain.StockRecord;
import com.server.InvestiMate.api.member.domain.Member;
import com.server.InvestiMate.common.auditing.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Blob;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "feedback_index") // Updated column name
    private Long index;

    @Column(nullable = false, name = "feedback_type")
    private String feedbackType;

    @Lob
    @Column(nullable = false, name = "data", columnDefinition = "TEXT")
    private String data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_record_id", nullable = false)
    private StockRecord stockRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public Feedback(Long feedbackIndex, String feedbackType, String data, StockRecord stockRecord, Member member) {
        this.index = feedbackIndex; // Use the updated name
        this.feedbackType = feedbackType;
        this.data = data;
        this.member = member;
        this.stockRecord = stockRecord;
    }

}
