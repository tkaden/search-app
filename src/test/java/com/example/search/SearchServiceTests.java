package com.example.search;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTests {

    @Mock
    private SearchRecordRepository repository;

    @InjectMocks
    private SearchService searchService;

    private SearchRecord record;

    @BeforeEach
    void setUp() {
        record = new SearchRecord();
        record.setUsername("TestTyler");
        record.setSearchTerm("TestTyler");
        record.setResultCount(10);
    }

    @Test
    public void testSaveSearchRecord() {
        when(repository.save(record)).thenReturn(record);

        SearchRecord savedRecord = searchService.savSearchRecord(record);

        verify(repository).save(record);
        assertThat(savedRecord).isEqualTo(record);
    }

    @Test
    public void testGetAllSearchRecords() {
        List<SearchRecord> records = Arrays.asList(record);
        when(repository.findAll()).thenReturn(records);

        List<SearchRecord> result = searchService.getAllSearchRecords();

        verify(repository).findAll();
        assertThat(result).hasSize(1);
        assertThat(result).contains(record);
    }
}
