package io.simpolor.mongodb.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SpringBootTest(classes = {MongoConfig.class})
public class MongoConfigTest {

    @Autowired
    private MongoConfig mongoConfig;

    @Test
    public void testConnection() {

        MongoClient client = (MongoClient) mongoConfig.mongoClient();

        assertThat(client).isNotNull();

        System.out.println("client.getAddress() : " + client.getAddress());
        for(MongoCredential credential : client.getCredentialsList()){
            System.out.println("credential : " + credential);
        }
    }
}
