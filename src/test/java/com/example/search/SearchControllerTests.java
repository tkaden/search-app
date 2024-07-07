package com.example.search;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class SearchControllerTests {

    @Mock
    private SearchService searchService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SearchController searchController;

    private SearchRecord record;

    @BeforeEach
    void setUp() {
        record = new SearchRecord();
        record.setUsername("TestUser");
        record.setSearchTerm("TestTerm");
        record.setResultCount(10);
    }

    @Test
    public void testSearchWithValidInputs() {
        // Mocking RestTemplate response
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("numFound", 5);
        JSONArray docs = new JSONArray();
        docs.put(new JSONObject().put("title", "Book1"));
        docs.put(new JSONObject().put("title", "Book2"));
        jsonResponse.put("docs", docs);

        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(ResponseEntity.ok(jsonResponse.toString()));

        // Mocking SearchService response
        when(searchService.saveSearchRecord(record)).thenReturn(record);

        // Calling the controller method directly
        ResponseEntity<?> response = searchController.search("TestUser", "TestTerm");

        verify(restTemplate).getForEntity(anyString(), eq(String.class));
        verify(searchService).saveSearchRecord(record);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertThat(responseBody.get("resultCount")).isEqualTo(5);
        assertThat(responseBody.get("titles")).isEqualTo(Arrays.asList("Book1", "Book2"));
    }

    @Test
    public void testSearchWithEmptyInputs() {
        ResponseEntity<?> response = searchController.search("", "");

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("Please populate both fields");
    }

    @Test
    public void testListPrevious() {
        // Mocking SearchService response
        List<SearchRecord> records = Collections.singletonList(record);
        when(searchService.getAllSearchRecords()).thenReturn(records);

        ResponseEntity<List<SearchRecord>> response = searchController.listPrevious();

        verify(searchService).getAllSearchRecords();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()).contains(record);
    }
}
