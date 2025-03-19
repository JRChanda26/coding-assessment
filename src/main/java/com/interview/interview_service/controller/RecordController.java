package com.interview.interview_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interview.interview_service.modal.RecordEntity;
import com.interview.interview_service.service.RecordService;

@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    RecordService recordService;

    @PostMapping("/addrecord")
    public ResponseEntity<?> addRecord(@RequestBody RecordEntity recordEntity) {
        RecordEntity data = recordService.addRecord(recordEntity);
        return new ResponseEntity<>(data, HttpStatus.OK);

    }

    @GetMapping("/getrecords")
    public ResponseEntity<?> getAllData() {
        List<RecordEntity> data = recordService.getAllData();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

}
