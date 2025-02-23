package io.simpolor.elasticsearchclient.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.simpolor.elasticsearchclient.repository.converter.StudentJsonConverter;
import io.simpolor.elasticsearchclient.repository.entity.Student;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class StudentRepositoryImpl implements StudentRepositoryCustom {

    private final RestHighLevelClient client;
    private final StudentJsonConverter converter;
    private final ObjectMapper objectMapper;

    public long totalCount(){
        SearchRequest searchRequest = new SearchRequest("student");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        searchRequest.source(sourceBuilder);

        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

            return Objects.requireNonNull(response.getHits().getTotalHits()).value;

        } catch (IOException e) {
            throw new ElasticsearchException("Failed to fetch student count", e);
        }
    }


    public List<Student> list(){
        SearchRequest searchRequest = new SearchRequest("student");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        searchRequest.source(sourceBuilder);

        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

            List<Student> students = new ArrayList<>();

            SearchHits searchHits = response.getHits();
            for(SearchHit searchHit : searchHits){
                Student student = converter.toStudent.apply(searchHit.getSourceAsString());
                student.setId(searchHit.getId());
                students.add(student);
            }

            return students;

        } catch (IOException e) {
            throw new ElasticsearchException("Failed to fetch student", e);
        }
    }

    public SearchAfter<Student> searchAfter(String searchAfter, Integer size){
        SearchRequest searchRequest = new SearchRequest("student");

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(boolQueryBuilder)
                .size(size)
                .sort(SortBuilders.fieldSort("createdAt").order(SortOrder.ASC))
                .timeout(new TimeValue(60, TimeUnit.SECONDS));

        if (!StringUtils.isEmpty(searchAfter)) {
            sourceBuilder.searchAfter(decodeSearchAfter(searchAfter));
        }
        searchRequest.source(sourceBuilder);

        try {
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

            List<Student> students = new ArrayList<>();

            SearchHits searchHits = response.getHits();
            for (SearchHit searchHit : searchHits) {
                Student student = converter.toStudent.apply(searchHit.getSourceAsString());
                student.setId(searchHit.getId());
                students.add(student);
            }

            int totalCount = (int) response.getHits().getTotalHits().value;
            int listCount = response.getHits().getHits().length;

            String key = "";
            if (listCount > 0) {
                Object[] lastSortValues = response.getHits().getHits()[listCount - 1].getSortValues();
                if (lastSortValues.length > 0) {
                    key = encodeSearchAfter(lastSortValues);
                }
            }

            return new SearchAfter<>(students, key, totalCount);

        } catch (IOException e) {
            throw new ElasticsearchException("Failed to fetch student with searchAfter", e);
        }
    }

    private Object[] decodeSearchAfter(String encoded) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encoded);
            return objectMapper.readValue(decodedBytes, Object[].class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to decode searchAfter key", e);
        }
    }

    // ✅ `search_after` 값을 인코딩 (Object[] → Base64)
    private String encodeSearchAfter(Object[] sortValues) {
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(sortValues);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to encode searchAfter key", e);
        }
    }

    @Override
    public Optional<Student> get(String studentId) {

        try {
            GetRequest getRequest = new GetRequest("student", studentId);
            GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);

            if (!response.isExists()) {
                return Optional.empty();
            }

            Student student = converter.toStudent.apply(response.getSourceAsString());
            student.setId(response.getId());

            return Optional.of(student);

        } catch (IOException e) {
            throw new ElasticsearchException("Failed to get student", e);
        }
    }


    public Student insert(Student student){
        String json = converter.toJson.apply(student);

        try {
            IndexRequest request =
                    new IndexRequest("student")
                            .source(json, XContentType.JSON);

            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            student.setId(response.getId());

            return student;

        } catch (IOException e) {
            throw new ElasticsearchException("Failed to insert student ", e);
        }
    }

    public Student update(Student student) {

        String json = converter.toJson.apply(student);

        try {
            UpdateRequest request =
                    new UpdateRequest("student", student.getId())
                            .doc(json, XContentType.JSON);

            UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
            if (response.getResult() == UpdateResponse.Result.NOOP) {
                throw new ElasticsearchException("No update performed for Student ID: " + student.getId());
            }

            return student;

        } catch (IOException e) {
            throw new ElasticsearchException("Failed to update student", e);
        }
    }

    public void delete(String studentId) {

        try {
            DeleteRequest request = new DeleteRequest("student", studentId);
            client.delete(request, RequestOptions.DEFAULT);

            /*DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
            switch (response.getResult()) {
                case DELETED:
                    return true;
                case NOT_FOUND:
                    throw new ElasticsearchException("Student ID not found: " + studentId);
                default:
                    return false;
            }*/

        } catch (IOException e) {
            throw new ElasticsearchException("Failed to delete student", e);
        }
    }
}
