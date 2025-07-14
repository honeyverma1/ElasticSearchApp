package Assignment.ElasticSearchApp.DTO;

import Assignment.ElasticSearchApp.entity.CourseDocument;

import java.util.List;

public record SearchResponse(
        List<CourseDocument> res,
        long total
) {
}
