package io.simpolor.mongo.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MongoConfig.class})
public class MongoConfigTest {

    @Autowired
    private MongoConfig mongoConfig;

    @Test
    public void testConnection() {
        MongoClient client = mongoConfig.mongoClient();

        assertThat(client).isNotNull();

        System.out.println("client.getAddress() : " + client.getAddress());
        for(MongoCredential credential : client.getCredentialsList()){
            System.out.println("credential : " + credential);
        }
    }

    @Test
    public void testMongoClient() {

        // given
        val mongoConfig = new MongoConfig();
        ReflectionTestUtils.setField(mongoConfig, "database", "test-db");
        ReflectionTestUtils.setField(mongoConfig, "host", "127.0.0.1");
        ReflectionTestUtils.setField(mongoConfig, "port", 27017);

        // when
        val client = mongoConfig.mongoClient();

        // then
        val database = client.getDatabase(mongoConfig.getDatabaseName());
        val pong = database.runCommand(new Document("ping", 2));
        Assertions.assertThat(pong).isEqualTo(new Document("ok", 1.0));

    }

}
