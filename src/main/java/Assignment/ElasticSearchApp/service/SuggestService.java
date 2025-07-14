package Assignment.ElasticSearchApp.service;

import co.elastic.clients.elasticsearch.core.search.FieldSuggester;
import co.elastic.clients.elasticsearch.core.search.SuggestFuzziness;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SuggestService {

    private final RestHighLevelClient client;

    public SuggestService(RestHighLevelClient client) {
        this.client = client;
    }


    public List<String> getSuggestions(String partial) throws IOException {
        List<String> suggestions = new ArrayList<>();


        CompletionSuggestionBuilder completionSuggestionBuilder = SuggestBuilders
                .completionSuggestion("suggest")
                .prefix(partial)
                .skipDuplicates(true)
                .size(10);

        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("course-suggest", completionSuggestionBuilder);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.suggest(suggestBuilder);

        SearchRequest searchRequest = new SearchRequest("coursedocument");
        searchRequest.source(sourceBuilder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        Suggest suggest = response.getSuggest();
        if(suggest != null) {
            Suggest.Suggestion<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> suggestion =
                    suggest.getSuggestion("course-suggest");
            for (Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option> entry : suggestion.getEntries()) {
                for (Suggest.Suggestion.Entry.Option option : entry) {
                    suggestions.add(option.getText().string());
                }
            }
        }

        return suggestions;
    }
}
