package io.simpolor.elasticsearch.repository;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.*;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class EmbeddedElasticsearchTest {

    static EmbeddedElastic embeddedElastic;

    static TransportClient client;

    @BeforeClass
    public static void beforeClass() throws Exception{
        embeddedElastic = EmbeddedElastic.builder()
                .withElasticVersion("5.6.3")
                .withSetting(PopularProperties.HTTP_PORT, 9200)
                .withSetting(PopularProperties.TRANSPORT_TCP_PORT, 9300)
                .withSetting(PopularProperties.CLUSTER_NAME, "elasticsearch")
                .withEsJavaOpts("-Xms128m -Xmx512m")
                .withIndex("student")
                .withStartTimeout(1, TimeUnit.MINUTES)
                .build();

        embeddedElastic.start();

        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch").build();

        client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
    }
    @After
    public void after(){
        embeddedElastic.stop();
        client.close();
    }

    @Test
    public void testEmbeddedElasticsearchCount(){
        SearchResponse searchResponse = client.prepareSearch("student")
                .setTypes("doc")
                .setSize(1000)
                .get();

        SearchHits hits = searchResponse.getHits();
        System.out.println("hits : "+hits.totalHits);
    }

    @Test
    public void testEmbeddedElasticsearchIndex(){
        Map<String, Object> json = new HashMap<>();
        json.put("name", "parksy");
        json.put("grade", "3");
        json.put("age", 19);
        json.put("hobby", Arrays.asList("축구", "컴퓨터"));

        IndexResponse indexResponse = client.prepareIndex("student", "doc")
                .setSource(json)
                .get();

        System.out.println("response.getId() : "+indexResponse.getId());

        GetResponse getResponse = client.prepareGet("student","doc", indexResponse.getId()).get();
        System.out.println("sourceAsString : "+getResponse.getSourceAsString());
    }

    @Test
    public void testEmbeddedElasticsearchGet(){
        GetResponse getResponse = client.prepareGet("student","doc", "AWsasow94g7pYAvrZB2K").get();
        System.out.println("sourceAsString : "+getResponse.getSourceAsString());
    }
}

