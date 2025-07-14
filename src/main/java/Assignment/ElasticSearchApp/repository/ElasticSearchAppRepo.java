package Assignment.ElasticSearchApp.repository;

import Assignment.ElasticSearchApp.entity.CourseDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ElasticSearchAppRepo extends ElasticsearchRepository<CourseDocument, String> {
    @Query("""
            {
                "bool": {
                    "should": [
                        {"match": {"title": "?0"}},
                        {"match": {"description": "?0"}}
                    ]
                }
            }
            """)
    List<CourseDocument> textSearch(String keyword);


}
