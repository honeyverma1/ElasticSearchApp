package Assignment.ElasticSearchApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartUpCourseIndexing implements CommandLineRunner {

    @Autowired
    private ElasticSearchAppServices elasticSearchAppServices;

    @Override
    public void run(String... args) {
        elasticSearchAppServices.bulkIndexing();
    }
}
