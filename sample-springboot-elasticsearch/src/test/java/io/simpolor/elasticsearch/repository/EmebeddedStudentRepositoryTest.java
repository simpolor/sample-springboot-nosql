package io.simpolor.elasticsearch.repository;

import io.simpolor.elasticsearch.ElasticsearchApplication;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticsearchApplication.class , webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmebeddedStudentRepositoryTest {

    EmbeddedElastic embeddedElastic = EmbeddedElastic.builder()
            .withElasticVersion("5.6.3")
            .withSetting(PopularProperties.HTTP_PORT, 9200)
            .withSetting(PopularProperties.TRANSPORT_TCP_PORT, 9300)
            .withSetting(PopularProperties.CLUSTER_NAME, "elasticsearch")
            .withEsJavaOpts("-Xms128m -Xmx512m")
            .withIndex("student")
            .withStartTimeout(1, TimeUnit.MINUTES)
            .build();

    @Before
    public void before() throws Exception{
        embeddedElastic.start();
    }
    @After
    public void after(){
        embeddedElastic.stop();
    }

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void test(){
        System.out.println("studentRepository.count() : "+studentRepository.count());
    }
}
