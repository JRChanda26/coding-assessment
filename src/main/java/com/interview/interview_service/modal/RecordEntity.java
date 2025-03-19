package com.interview.interview_service.modal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "record_entity")
public class RecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long recordId;
    public String recordLink;

    @OneToOne(mappedBy = "interviewRecord")
    private InterviewEntity interviewEntity;
}
