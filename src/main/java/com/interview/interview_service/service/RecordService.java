package com.interview.interview_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interview.interview_service.modal.RecordEntity;
import com.interview.interview_service.repository.RecordRepository;

@Service
public class RecordService {

    @Autowired
    RecordRepository recordRepository;

    public RecordEntity addRecord(RecordEntity recordEntity) {
        RecordEntity data = recordRepository.save(recordEntity);
        return data;

    }

    public List<RecordEntity> getAllData() {

        List<RecordEntity> data = recordRepository.findAll();
        return data;

    }

}
