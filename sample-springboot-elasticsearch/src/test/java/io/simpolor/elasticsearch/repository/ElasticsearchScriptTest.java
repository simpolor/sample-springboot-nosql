package io.simpolor.elasticsearch.repository;

import io.simpolor.elasticsearch.ElasticsearchApplication;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.reindex.*;
import org.elasticsearch.script.Script;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.idsQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticsearchApplication.class , webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ElasticsearchScriptTest {

    @Autowired
    private Client client;

    @Test
    public void testElasticsearchScript() throws Exception {
        Map<String, Object> json = new HashMap<>();
        json.put("name", "parksy");
        json.put("grade", "3");
        json.put("age", 19);
        json.put("hobby", Arrays.asList("축구", "컴퓨터"));

        IndexResponse indexResponse = client.prepareIndex("student", "doc")
                .setSource(json)
                .get();

        System.out.println("response.getId() : " + indexResponse.getId());

        Script script = new Script("ctx._source.age = 20");

        UpdateByQueryRequestBuilder ubqrb = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
        BulkByScrollResponse r = ubqrb.source("student").script(script)
                .filter(termQuery("name", "parksy")).execute().get();

        BulkByScrollResponse response = ubqrb.get();

        for (int i = 0; i <= 10; i++) {
            GetResponse getResponse = client.prepareGet("student", "doc", indexResponse.getId()).get();
            System.out.println("sourceAsString : " + getResponse.getSourceAsString());
            Thread.sleep(1000);
        }

    }

    @Test
    public void testElasticsearchScript2() throws Exception {

        Map<String, Object> json = new HashMap<>();
        json.put("name", "parksy");
        json.put("grade", "3");
        json.put("age", 19);
        json.put("hobby", Arrays.asList("축구", "컴퓨터"));

        IndexResponse indexResponse = client.prepareIndex("student", "doc")
                .setSource(json)
                .get();

        System.out.println("response.getId() : " + indexResponse.getId());

        Script script = new Script("ctx._source.age = 20");

        UpdateByQueryRequestBuilder ubqrb = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
        ubqrb.source("student").source().setTypes("doc");
        BulkByScrollResponse r = ubqrb
                .abortOnVersionConflict(false)
                .waitForActiveShards(ActiveShardCount.ALL)
                .script(new Script("ctx._source.age = 20"))
                .filter(idsQuery().types("doc").addIds(indexResponse.getId()))
                .setMaxRetries(10)
                .execute().get();

        // RefreshRequest request = new RefreshRequest("student");
        // RefreshRequest requestAll = new RefreshRequest();
        // client.admin().indices().prepareRefresh().get();

        BulkByScrollResponse response = ubqrb.get();

        for (int i = 0; i <= 10; i++) {
            GetResponse getResponse = client.prepareGet("student", "doc", indexResponse.getId()).get();
            System.out.println("sourceAsString : " + getResponse.getSourceAsString());
            Thread.sleep(1000);
        }

        client.close();

    }

}