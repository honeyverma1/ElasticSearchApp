package Assignment.ElasticSearchApp.controller;


import Assignment.ElasticSearchApp.DTO.SearchResponse;
import Assignment.ElasticSearchApp.entity.CourseDocument;
import Assignment.ElasticSearchApp.service.ElasticSearchAppServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ElasticSearchAppController {

    @Autowired
    private ElasticSearchAppServices elasticSearchAppServices;

    @GetMapping("/health-check")
    public ResponseEntity<?> healthcheck(){
        return new ResponseEntity<>("hurray! the api is running fine!!", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<SearchResponse> search (
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(defaultValue = "upcoming") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ) {
        SearchResponse searchResponse = elasticSearchAppServices.advancedSearch(q, minAge, maxAge, category, type, minPrice, maxPrice, startDate, sort, page, size);
        return new ResponseEntity<>(searchResponse, HttpStatus.OK);
    }
}
