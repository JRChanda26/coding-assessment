package com.interview.interview_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.interview.interview_service.modal.RecordEntity;

@Repository
public interface RecordRepository extends JpaRepository<RecordEntity , Long>  {

    
}  
