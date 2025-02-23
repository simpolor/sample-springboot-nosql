package io.simpolor.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RedissionConfig {

    @Value("${redisson.database}")
    private Integer database;

    @Value("${redisson.hostname}")
    private String hostname;

    @Value("${redisson.port}")
    private Integer port;

    @Value("${redisson.password}")
    private String password;

    @Value("${redisson.cluster-enabled}")
    private Boolean clusterEnabled;

    @Value("${redisson.timeout}")
    private Integer timeout;

    @Value("${redisson.pool.max-total}")
    private Integer poolMaxTotal;

    @Value("${redisson.pool.max-idle}")
    private Integer poolMaxIdle;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {

        Config config = new Config();
        config.setCodec(new JsonJacksonCodec(mapper()));

        // 성능 테스트시 기본값으로하면 병목이 발생한다.
        // config.setNettyThreads(64);

        if (clusterEnabled) {
            if(!password.isEmpty()){
                config.useClusterServers().setPassword(password);
            }

            config.useClusterServers().addNodeAddress("redis://" + hostname + ":" + port);

            // redis max idle 갯수를 redission에서 min idle 갯수로 사용한다. 기본값은 10이다.
            config.useClusterServers().setMasterConnectionMinimumIdleSize(10);
            config.useClusterServers().setSlaveConnectionMinimumIdleSize(10);

            config.useClusterServers().setMasterConnectionPoolSize(100);
            config.useClusterServers().setSlaveConnectionPoolSize(100);

            // remote execute 서비스에서 read from replica 설정을 하면 queue에서 받았으나 hashs에 데이터가 없는 상황이 발생한다.
            config.useClusterServers().setReadMode(ReadMode.MASTER);

            config.useClusterServers().setConnectTimeout(1000);
            config.useClusterServers().setTimeout(1000);

            config.useClusterServers().setSubscriptionConnectionMinimumIdleSize(10);
            config.useClusterServers().setSubscriptionsPerConnection(100);

            config.useClusterServers().setSubscriptionMode(SubscriptionMode.MASTER);


        } else {
            if(!password.isEmpty()){
                config.useSingleServer().setPassword(password);
            }

            config.useSingleServer().setAddress("redis://" + hostname + ":" + port);
            config.useSingleServer().setDatabase(database);

            // redis max idle 갯수를 redission에서 min idle 갯수로 사용한다. 기본값은 10이다.
            config.useSingleServer().setConnectionMinimumIdleSize(10);
            config.useSingleServer().setConnectionPoolSize(100);

            config.useSingleServer().setConnectTimeout(1000);
            config.useSingleServer().setTimeout(1000);

            config.useSingleServer().setSubscriptionConnectionMinimumIdleSize(10);
            config.useSingleServer().setSubscriptionsPerConnection(100);
            // config.useSingleServer().setClientName("stats");
        }

        return Redisson.create(config);
    }

    /**
     * redisson 소스를 확인해보면, 설정에서 지정한 codec 인스턴스를 모든 곳에서 사용하는 것이 아니라,<br/>
     * 필요에 따라 codec의 class를 획득하여 ClassLoader를 인자로 받는 생성자로 새로 생성해서 사용하고 있다.<br/>
     * thread safe 하지 않는 코덱을 배려하기 위한 것 이겠으나 명백히 버그이다.<br/>
     * 우리가 원하는 코덱의 설정을 ClassLoader를 인자로 받는 생성자로 생성 할 수 있도록 workaround한다.
     *
     * @author kshong
     *
     */
    public static class JsonJacksonCodecSupportingJavaTime extends JsonJacksonCodec {
        public JsonJacksonCodecSupportingJavaTime(ClassLoader classLoader) {
            super(createObjectMapper(classLoader, mapper()));
        }

    }

    @Bean
    public static ObjectMapper mapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.findAndRegisterModules();

        return mapper;

    }
}
