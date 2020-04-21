package io.simpolor.elasticsearch.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.simpolor.elasticsearch.ElasticsearchApplication;
import io.simpolor.elasticsearch.domain.Student;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticsearchApplication.class , webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ElasticClientTest {

    public static final String INDEX = "student-test";
    public static final String TYPE = "student-test";

    @Autowired
    private Client client;

    @Autowired
    private StudentRepository studentRepository;

    @Before
    public void before() {

        ObjectMapper objectMapper = new ObjectMapper();

        List<Student> students = new ArrayList<>();
        students.add(new Student("1", 1, "name11", 3, 19, Arrays.asList()));
        students.add(new Student("2", 2, "name22", 2, 18, Arrays.asList()));
        students.add(new Student("3", 3, "name33", 1, 17, Arrays.asList()));

        BulkRequestBuilder deleteBulkRequest = client.prepareBulk();
        BulkRequestBuilder insertBulkRequest = client.prepareBulk();

        for(Student student : students){
            deleteBulkRequest.add(client.prepareDelete(INDEX, TYPE, student.getId()));
            insertBulkRequest.add(client.prepareIndex(INDEX, TYPE, student.getId())
                    .setSource(toJson(objectMapper, student).get(), XContentType.JSON));
        }
        BulkResponse deleteBulkResponse = deleteBulkRequest.get();
        if(deleteBulkResponse.hasFailures()) return;

        BulkResponse insertBulkResponse = insertBulkRequest.get();
        if(insertBulkResponse.hasFailures()) return;
    }

    @Test
    public void test(){

        GetResponse response = client.prepareGet(INDEX, TYPE, "1").get();
        System.out.println("response : "+response.getSourceAsString());

    }

    @Test
    public void test2(){

        QueryBuilder qb = QueryBuilders.boolQuery();

        SearchResponse searchResponse = client.prepareSearch(INDEX).setTypes(TYPE)
                .setQuery(qb)
                .get();

            for (SearchHit hit : searchResponse.getHits().getHits()) {
                System.out.println("response : "+hit.getSourceAsString());
            }
    }

    @Test
    public void test3(){

        QueryBuilder qb = QueryBuilders.boolQuery();

        SearchResponse searchResponse = client.prepareSearch(INDEX).setTypes(TYPE)
                .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                .setScroll(new TimeValue(60000))
                .setQuery(qb)
                .setSize(2)
                .get();

        do {
            System.out.println("scroll.. ");
            for (SearchHit hit : searchResponse.getHits().getHits()) {
                System.out.println("response : "+hit.getSourceAsString());
            }

            searchResponse = client.prepareSearchScroll(searchResponse.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
        } while(searchResponse.getHits().getHits().length != 0);

    }

    private static Optional<String> toJson(final ObjectMapper mapper, final Object source) {

        try {
            return Optional.ofNullable(mapper.writeValueAsString(source));

        } catch (final Exception e) {
            log.warn("Unable to writeValueAsString source", e);
        }

        return Optional.empty();
    }
}
