package com.example.search;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Test
    public void testSearchWithValidInputs() {
        // Mocking RestTemplate response
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("numFound", 5);
        JSONArray docs = new JSONArray();
        docs.put(new JSONObject().put("title", "Book1"));
        docs.put(new JSONObject().put("title", "Book2"));
        jsonResponse.put("docs", docs);

        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(ResponseEntity.ok(jsonResponse.toString()));

        // Mocking SearchService response
        SearchRecord record = new SearchRecord();
        record.setUsername("TestUser");
        record.setSearchTerm("TestTerm");
        record.setResultCount(5);
        when(searchService.saveSearchRecord(any(SearchRecord.class))).thenReturn(record);

        // Calling the controller method directly
        ResponseEntity<?> response = searchController.search("TestUser", "TestTerm", "username");

        verify(restTemplate).getForEntity(anyString(), any(Class.class));
        verify(searchService).saveSearchRecord(any(SearchRecord.class));

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertThat(responseBody.get("resultCount")).isEqualTo(5);
        assertThat(responseBody.get("titles")).isEqualTo(List.of("Book1", "Book2"));
    }

    @Test
    public void testSearchWithEmptyInputs() {
        // Calling the controller method directly with empty inputs
        ResponseEntity<?> response = searchController.search("", "", "");

        // Verify response status and body
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("Please populate both fields");
    }

    @Test
    public void testListPrevious() {
        // Mocking SearchService response
        SearchRecord record = new SearchRecord();
        record.setUsername("TestUser");
        record.setSearchTerm("TestTerm");
        record.setResultCount(5);
        List<SearchRecord> records = Collections.singletonList(record);
        when(searchService.getAllSearchRecords()).thenReturn(records);

        // Calling the controller method directly
        ResponseEntity<List<SearchRecord>> response = searchController.listPrevious("username");

        verify(searchService).getAllSearchRecords();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()).contains(record);
    }
}
