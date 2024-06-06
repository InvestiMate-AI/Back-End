package com.server.InvestiMate.api.chat.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "report_year")
    private Integer reportYear;

    @Column(name = "report_company")
    private String reportCompany;

    @Column(name = "report_type")
    private String reportType;

    @Column(name = "assistant_id")
    private String assistantsId;

    @OneToMany(mappedBy = "report")
    private List<Thread> threads = new ArrayList<>();

    public void createAssistantId(String assistantsId) { this.assistantsId = assistantsId;}
}
