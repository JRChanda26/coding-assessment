package com.interview.interview_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.interview.interview_service.modal.InterviewEntity;

@Repository
public interface InterviewRepository extends JpaRepository<InterviewEntity, Long> {

    Optional<InterviewEntity> findByUserInterviewId(String userInterviewId);

    Optional<InterviewEntity> findByEmail(String email);

    Optional<InterviewEntity> findByEmailAndPassword(String email, String password);

    List<InterviewEntity> findBySchedulingEmailTrueAndIsScheduledTrue();

    List<InterviewEntity> findBySchedulingEmailTrueAndIsScheduledTrueAndIsInterviewLinkEmailSentFalse();

    List<InterviewEntity> findBySchedulingEmailTrueAndIsScheduledTrueAndIsInterviewLinkEmailSentTrue();

    List<InterviewEntity> findByIsInterviewLinkEmailSentTrue();

    List<InterviewEntity> findByIsTokenGeneratedTrue();
}
