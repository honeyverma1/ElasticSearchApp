package Assignment.ElasticSearchApp.service;

import Assignment.ElasticSearchApp.DTO.SearchResponse;
import Assignment.ElasticSearchApp.entity.CourseDocument;
import Assignment.ElasticSearchApp.repository.ElasticSearchAppRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ElasticSearchAppServices {

    private final RestHighLevelClient restHighLevelClient;

    private static final Logger log = LoggerFactory.getLogger(ElasticSearchAppServices.class);


    @Autowired
    private ElasticSearchAppRepo elasticSearchAppRepo;

    @Autowired
    private ObjectMapper objectMapper;

    public ElasticSearchAppServices(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

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
    ) throws IOException {
        SearchRequest searchRequest = new SearchRequest("coursedocument");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if(q != null && !q.isBlank()) {
            boolQuery.should(QueryBuilders.matchQuery("title", q).fuzziness(Fuzziness.AUTO));
            boolQuery.should(QueryBuilders.matchQuery("description", q));
        }

        if(category != null && !category.isBlank()) {
            boolQuery.filter(QueryBuilders.termQuery("category", category));
        }

        if (type != null && !type.isBlank()) {
            boolQuery.filter(QueryBuilders.termQuery("type", type));
        }

        if(minAge != null) {
            boolQuery.filter(QueryBuilders.rangeQuery("minAge").gte(minAge));
        }

        if(maxAge != null) {
            boolQuery.filter(QueryBuilders.rangeQuery("maxAge").lte(maxAge));
        }

        if(minPrice != null) {
            boolQuery.filter(QueryBuilders.rangeQuery("price").gte(minPrice));
        }

        if(maxPrice != null) {
            boolQuery.filter(QueryBuilders.rangeQuery("price").lte(maxPrice));
        }

        if (startDate != null) {
            boolQuery.filter(QueryBuilders.rangeQuery("nextSessionDate").gte(startDate));
        }

        Sort sortOrder = Sort.unsorted();

        if (sort != null && !sort.isBlank()) {
            switch (sort) {
                case "upcoming" -> sourceBuilder.sort("nextSessionDate", SortOrder.ASC);
                case "priceAsc" -> sourceBuilder.sort("price", SortOrder.ASC);
                case "priceDesc" -> sourceBuilder.sort("price", SortOrder.DESC);
            }
        }

        sourceBuilder.from(page * size);
        sourceBuilder.size(size);
        sourceBuilder.query(boolQuery);

        searchRequest.source(sourceBuilder);

        org.elasticsearch.action.search.SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);


        List<CourseDocument> courses = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits()) {
            String json = hit.getSourceAsString();
            CourseDocument doc = objectMapper.readValue(json, CourseDocument.class);
            courses.add(doc);
        }

        return new SearchResponse(courses, searchResponse.getHits().getTotalHits().value);
    }
}
