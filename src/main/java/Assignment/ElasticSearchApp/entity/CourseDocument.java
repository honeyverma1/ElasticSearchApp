package Assignment.ElasticSearchApp.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.suggest.Completion;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@Document(indexName = "coursedocument")
public class CourseDocument {


    @Id
    private String id;
    @Field(type = FieldType.Text)
    private String title;
    @Field(type = FieldType.Text)
    private String description;
    @Field(type = FieldType.Keyword)
    private String category;
    @Field(type = FieldType.Keyword)
    private String type;
    @Field(type = FieldType.Keyword)
    private String gradeRange;
    @Field(type = FieldType.Integer)
    private int minAge;
    @Field(type = FieldType.Integer)
    private int maxAge;
    @Field(type = FieldType.Double)
    private double price;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime nextSessionDate;

    @CompletionField
    private Completion suggest;


    public CourseDocument(String id, String title, String description, String category, String type, String gradeRange, int minAge, int maxAge, double price, LocalDateTime nextSessionDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.type = type;
        this.gradeRange = gradeRange;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.price = price;
        this.nextSessionDate = nextSessionDate;
    }

    public Completion getSuggest() {
        return suggest;
    }
    public void setSuggest(Completion suggest) {
        this.suggest = suggest;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGradeRange() {
        return gradeRange;
    }

    public void setGradeRange(String gradeRange) {
        this.gradeRange = gradeRange;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getNextSessionDate() {
        return nextSessionDate;
    }

    public void setNextSessionDate(LocalDateTime nextSessionDate) {
        this.nextSessionDate = nextSessionDate;
    }
}
