package io.simpolor.elasticsearch.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.simpolor.elasticsearch.domain.SearchAfter;
import io.simpolor.elasticsearch.domain.Student;
import io.simpolor.elasticsearch.util.Base64;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.ElasticsearchException;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class SearchAfterStudentRepository {

    public static final String HISTORY_SEARCHAFTER_REGEX = "-";

    @Autowired
    private Client client;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private ObjectMapper objectMapper;

    Function<Student,String> toJson = new Function<Student, String>() {
        @Override
        public String apply(Student student) {
            String toJsonSource;
            try {
                toJsonSource = objectMapper.writeValueAsString(student);
            } catch (JsonProcessingException e) {
                throw new ElasticsearchException(e.getMessage());
            }
            return toJsonSource;
        }
    };

    Function<String, Student> toStudent = new Function<String, Student>() {
        @Override
        public Student apply(String json) {
            Student student = null;
            try {
                student = objectMapper.readValue(json, Student.class);
            } catch (IOException e) {
                throw new ElasticsearchException(e.getMessage());
            }
            return student;
        }
    };

    public long selectStudentTotalCount(){

        SearchResponse response
                = client.prepareSearch("student")
                    .setTypes("doc")
                    .get();

        return response.getHits().totalHits;
    }

    public SearchAfter<Student> selectStudentList(String searchAfter, int size){

        ObjectMapper objectMapper = new ObjectMapper();

        // 검색 조건
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // boolQueryBuilder.must(termQuery("uid", id));

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(boolQueryBuilder)
                .size(size)
                .sort(SortBuilders.fieldSort("seq").order(SortOrder.ASC))
                .timeout(new TimeValue(60, TimeUnit.SECONDS));

        if(!StringUtils.isEmpty(searchAfter)){
            sourceBuilder.searchAfter(decodeSearchAfter(searchAfter));
        }

        SearchResponse response = client.prepareSearch("student")
                .setTypes("doc")
                .setSource(sourceBuilder)
                .get();

        List<Student> students = new ArrayList<>();
        SearchHit[] searchHits = response.getHits().getHits();

        try {
            for (SearchHit hit : searchHits) {
                Student student = objectMapper.readValue(hit.getSourceAsString(), Student.class);
                students.add(student);
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }

        int totalCount = (int)response.getHits().getTotalHits();
        int listCount = response.getHits().getHits().length;

        String key = "";
        if(listCount > 0){
            Object[] lastSortValues = Arrays.stream(response.getHits().getHits())
                    .reduce((first, second) -> second)
                    .map(s -> s.getSortValues())
                    .get();

            key = encodeSearchAfter(lastSortValues);
        }

        return new SearchAfter(students, key, totalCount);
    }

    public Student selectStudent(String id){
        Client client = elasticsearchOperations.getClient();
        GetResponse response = client.prepareGet("student", "doc", id).get();

        if(response.isExists()){
            return toStudent.apply(response.getSourceAsString());
        }
        return new Student();
    }

    public Student insertStudent(Student student){

        Map<String, Object> json = new HashMap<>();
        json.put("seq", student.getSeq());
        json.put("name", student.getName());
        json.put("grade",student.getGrade());
        json.put("age", student.getAge());
        json.put("hobby", student.getHobby());

        IndexResponse response
                = client.prepareIndex("student", "doc")
                    .setSource(json)
                    .get();

        student.setId(response.getId());

        return student;
    }

    public Student updateStudent(Student student){

        Map<String, Object> json = new HashMap<>();
        json.put("seq", student.getSeq());
        json.put("name", student.getName());
        json.put("grade",student.getGrade());
        json.put("age", student.getAge());
        json.put("hobby", student.getHobby());

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("student");
        updateRequest.type("doc");
        updateRequest.id(student.getId());
        updateRequest.doc(json);

        client.update(updateRequest).actionGet();

        return student;
    }

    public void deleteStudent(String id){
        DeleteResponse response = client.prepareDelete("student", "doc", id).get();
        System.out.println("DeleteResponse.getId : "+response.getId());
    }

    public Object[] decodeSearchAfter(String searchAfter){
        byte[] decodeBytes =  Base64.decode(searchAfter.getBytes());
        String decode = new String(decodeBytes);

        return Arrays.stream(decode.split(HISTORY_SEARCHAFTER_REGEX)).toArray();
    }

    private String encodeSearchAfter(Object[] sortValues) {
        if(sortValues.length > 0){
            String searchAfter = Arrays.stream(sortValues).map(Object::toString).collect(Collectors.joining(HISTORY_SEARCHAFTER_REGEX));
            return new String(Base64.encode(searchAfter.getBytes()));
        }
        return StringUtils.EMPTY;
    }
}
