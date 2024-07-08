package com.example.search;

import java.util.ArrayList;
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
    public ResponseEntity<?> search(@RequestParam String username, @RequestParam String searchTerm) {
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
        SearchRecord record = new SearchRecord();
        record.setUsername(username);
        record.setSearchTerm(searchTerm);
        record.setResultCount(numFound);
        service.saveSearchRecord(record);

        // Create Response
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("resultCount", numFound);
        responseBody.put("titles", titles);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/previous")
    public ResponseEntity<List<SearchRecord>> listPrevious() {
        return ResponseEntity.ok(service.getAllSearchRecords());
    }
}
