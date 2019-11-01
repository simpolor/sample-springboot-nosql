package io.simpolor.elasticsearch.repository;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.reindex.*;
import org.elasticsearch.script.Script;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.*;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

public class EmbeddedElasticsearchScriptTest {

    static EmbeddedElastic embeddedElastic;

    static TransportClient client;

    @BeforeClass
    public static void beforeClass() throws Exception{
        embeddedElastic = EmbeddedElastic.builder()
                .withElasticVersion("5.6.3")
                .withSetting(PopularProperties.HTTP_PORT, 9201)
                .withSetting(PopularProperties.TRANSPORT_TCP_PORT, 9301)
                .withSetting(PopularProperties.CLUSTER_NAME, "elasticsearch")
                .withEsJavaOpts("-Xms128m -Xmx512m")
                .withIndex("student")
                .withStartTimeout(1, TimeUnit.MINUTES)
                .build();

        embeddedElastic.start();

        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch").build();

        client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9301));
    }
    @AfterClass
    public static void after(){
        client.close();
        embeddedElastic.stop();
    }

    @Test
    public void testEmbeddedElasticsearchScript() throws Exception{
        Map<String, Object> json = new HashMap<>();
        json.put("name", "parksy");
        json.put("grade", "3");
        json.put("age", 19);
        json.put("hobby", Arrays.asList("축구", "컴퓨터"));

        IndexResponse indexResponse = client.prepareIndex("student", "doc")
                .setSource(json)
                .get();

        System.out.println("response.getId() : "+indexResponse.getId());

        StringBuilder sbScript = new StringBuilder();
        sbScript.append("ctx._source.age = 20;");
        Script script = new Script("ctx._source.age = 20");

        /*client.prepareUpdate("student", "doc", indexResponse.getId())
                .setScript(new Script(sbScript.toString()))
                .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE)
                .execute();*/

        /*UpdateByQueryRequestBuilder ubqrb = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
        BulkByScrollResponse r = ubqrb.source("student").script(script)
                .filter(termQuery("name", "parksy")).execute().get();

        BulkByScrollResponse response = ubqrb.get();*/

        //UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
        //BulkByScrollResponse r = updateByQuery.source("student")
        //        .script(script)
        //        .filter(termQuery("name", "parksy"))
        //        .get();
        //.size(1000)
        //.script(new Script(sbScript.toString()));
                /*.script(new Script(ScriptType.INLINE,
                        "ctx._source.age = 20",
                        "painless",
                        Collections.emptyMap()));*/

        //BulkByScrollResponse response = updateByQuery.get();

        UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
        BulkByScrollResponse r = updateByQuery.source("student").script(script).filter(termQuery("name", "parksy")).execute().get();

        BulkByScrollResponse response = updateByQuery.get();

        for(int i=0; i<=10;i++){
            GetResponse getResponse = client.prepareGet("student","doc", indexResponse.getId()).get();
            System.out.println("sourceAsString : "+getResponse.getSourceAsString());
            Thread.sleep(1000);
        }

    }

    @Test
    public void testEmbeddedElasticsearchScript2() throws Exception{
        Map<String, Object> json = new HashMap<>();
        json.put("name", "parksy");
        json.put("grade", "3");
        json.put("age", 19);
        json.put("hobby", Arrays.asList("축구", "컴퓨터"));

        IndexResponse indexResponse = client.prepareIndex("student", "doc")
                .setSource(json)
                .get();

        System.out.println("response.getId() : "+indexResponse.getId());

        Script script = new Script("ctx._source.age = 20");

        UpdateByQueryRequestBuilder ubqrb = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
        BulkByScrollResponse r = ubqrb.source("student").script(script)
                .filter(termQuery("name", "parksy")).execute().get();

        //BulkByScrollResponse response = ubqrb.get();

        for(int i=0; i<=10;i++){
            GetResponse getResponse = client.prepareGet("student","doc", indexResponse.getId()).get();
            System.out.println("sourceAsString : "+getResponse.getSourceAsString());
            Thread.sleep(1000);
        }

    }

}

