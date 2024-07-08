package com.example.search;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class SearchController {

    @Autowired
    private SearchService service;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam String username, 
            @RequestParam String searchTerm, 
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) boolean isSorting) {
                
        if (username.isEmpty() || searchTerm.isEmpty()) {
            return ResponseEntity.badRequest().body("Please populate both fields");
        }

        // Call Open Library
        String url = "https://openlibrary.org/search.json?q=" + searchTerm;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Parse
        JSONObject json = new JSONObject(response.getBody());
        int numFound = json.getInt("numFound");
        JSONArray docs = json.getJSONArray("docs");
        List<String> titles = new ArrayList<>();

        // Display 10 titles
        for (int i = 0; i < Math.min(10, docs.length()); i++) {
            titles.add(docs.getJSONObject(i).getString("title"));
        }

        // Save record
        if (!isSorting) {
            SearchRecord record = new SearchRecord();
            record.setUsername(username);
            record.setSearchTerm(searchTerm);
            record.setResultCount(numFound);
            service.saveSearchRecord(record);
        }

        // Sort titles if sortBy is provided
        if (sortBy != null) {
            switch (sortBy) {
                case "title":
                    titles.sort(String::compareToIgnoreCase);
                    break;
                case "username":
                    break;
                case "resultCount":
                    break;
                default:
                    break;
            }
        }

        // Create Response
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("resultCount", numFound);
        responseBody.put("titles", titles);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/previous")
    public ResponseEntity<List<SearchRecord>> listPrevious(@RequestParam(required = false) String sortBy) {
        List<SearchRecord> records = service.getAllSearchRecords();

        // Sort records if sortBy is provided
        if (sortBy != null) {
            switch (sortBy) {
                case "username":
                    records.sort(Comparator.comparing(SearchRecord::getUsername, String.CASE_INSENSITIVE_ORDER));
                    break;
                case "searchTerm":
                    records.sort(Comparator.comparing(SearchRecord::getSearchTerm, String.CASE_INSENSITIVE_ORDER));
                    break;
                case "resultCount":
                    records.sort(Comparator.comparingInt(SearchRecord::getResultCount));
                    break;
                default:
                    break;
            }
        }

        return ResponseEntity.ok(records);
    }
}
