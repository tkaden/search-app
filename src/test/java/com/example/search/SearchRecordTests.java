package com.example.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class SearchRecordTests {

    @Test
    public void testGetAndSetId() {
        SearchRecord record = new SearchRecord();
        Long id = 1L;
        record.setId(id);
        assertEquals(id, record.getId());
    }

    @Test
    public void testGetAndSetUsername() {
        SearchRecord record = new SearchRecord();
        String username = "TylerTest";
        record.setUsername(username);
        assertEquals(username, record.getUsername());
    }

    @Test
    public void testGetAndSetSearchTerm() {
        SearchRecord record = new SearchRecord();
        String searchTerm = "TylerTest";
        record.setSearchTerm(searchTerm);
        assertEquals(searchTerm, record.getSearchTerm());
    }

    @Test
    public void testGetAndSetResultCount() {
        SearchRecord record = new SearchRecord();
        int resultCount = 100;
        record.setResultCount(resultCount);
        assertEquals(resultCount, record.getResultCount());
    }
}