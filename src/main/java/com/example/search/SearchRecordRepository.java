package com.example.search;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRecordRepository extends JpaRepository<SearchRecord, Long> {
}