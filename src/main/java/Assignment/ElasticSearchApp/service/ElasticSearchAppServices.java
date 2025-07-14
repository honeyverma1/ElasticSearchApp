package Assignment.ElasticSearchApp.service;


import Assignment.ElasticSearchApp.DTO.SearchResponse;
import Assignment.ElasticSearchApp.entity.CourseDocument;
import Assignment.ElasticSearchApp.repository.ElasticSearchAppRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ElasticSearchAppServices {

    private final ElasticsearchOperations elasticsearchOperations;

    public ElasticSearchAppServices(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    private static final Logger log = LoggerFactory.getLogger(ElasticSearchAppServices.class);


    @Autowired
    private ElasticSearchAppRepo elasticSearchAppRepo;

    @Autowired
    private ObjectMapper objectMapper;

    public void bulkIndexing() {
        if(elasticSearchAppRepo.count() > 0) {
            log.info("Already Indexed all data");
            return;
        }
        try(InputStream inputStream = getClass().getResourceAsStream("/sample-courses.json")) {
            List<CourseDocument> courses = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<CourseDocument>>() {}
            );
            elasticSearchAppRepo.saveAll(courses);
            log.info("All data Indexed Successfully! ");
        } catch (Exception e) {
            log.error("Cannot Index Data", e);
        }
    }


    public SearchResponse advancedSearch(
            String q,
            Integer minAge,
            Integer maxAge,
            String category,
            String type,
            Double minPrice,
            Double maxPrice,
            LocalDateTime startDate,
            String sort,
            int page,
            int size
    ) {
        Criteria criteria = new Criteria();

        if(q != null && !q.isBlank()) {
            Criteria temp = new Criteria("title").matches(q)
                    .or(new Criteria("description").matches(q));
            criteria = criteria.and(temp);
        }

        if(minAge != null) {
            criteria.and(new Criteria("minAge").greaterThanEqual(minAge));
        }

        if(maxAge != null) {
            criteria.and(new Criteria("maxAge").lessThanEqual(maxAge));
        }

        if(category != null && !category.isBlank()) {
            criteria.and(new Criteria("category").matches(category));
        }

        if (type != null && !type.isBlank()) {
            criteria = criteria.and(new Criteria("type").is(type));
        }

        if(minPrice != null) {
            criteria.and(new Criteria("price").greaterThanEqual(minPrice));
        }

        if(maxPrice != null) {
            criteria.and(new Criteria("price").lessThanEqual(maxPrice));
        }

        if (startDate != null) {
            criteria = criteria.and(new Criteria("nextSessionDate").greaterThanEqual(startDate));
        }

        Sort sortOrder = Sort.unsorted();

        if(sortOrder != null && sort.isBlank()) {
            switch (sort) {
                case "upcoming" -> sortOrder = Sort.by(Sort.Direction.ASC, "nextSessionDate");
                case "priceAsc" -> sortOrder = Sort.by(Sort.Direction.ASC, "price");
                case "priceDesc" -> sortOrder = Sort.by(Sort.Direction.DESC, "price");
            }
        }

        CriteriaQuery query = new CriteriaQuery(criteria);
        query.setPageable(PageRequest.of(page, size));
        query.addSort(sortOrder);

        SearchHits<CourseDocument> hits = elasticsearchOperations.search(query, CourseDocument.class);

        log.info("Executing search with criteria: {}", criteria);

        List<CourseDocument> docs = hits.getSearchHits().stream()
                .map(hit -> hit.getContent())
                .toList();
        return new SearchResponse(docs, hits.getTotalHits());
    }
}
