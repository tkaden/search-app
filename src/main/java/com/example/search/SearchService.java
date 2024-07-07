package com.example.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
    
    @Autowired
    private SearchRecordRepository repository;

    public SearchRecord savSearchRecord(SearchRecord record) {
        return repository.save(record);
    }

    public List<SearchRecord> getAllSearchRecords() {
        return repository.findAll();
    }
}