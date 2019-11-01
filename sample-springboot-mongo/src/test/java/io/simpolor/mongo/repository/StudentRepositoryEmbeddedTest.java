package io.simpolor.mongo.repository;

import com.mongodb.*;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.*;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import io.simpolor.mongo.domain.Student;
import org.assertj.core.api.Assertions;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
public class StudentRepositoryEmbeddedTest {

    private MongodExecutable mongodExecutable;
    private MongoTemplate mongoTemplate;

    @Before
    public void setup() throws Exception {
        String ip = "localhost";
        int port = 27027;

        IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
                .net(new Net(ip, port, Network.localhostIsIPv6()))
                .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
        mongoTemplate = new MongoTemplate(new MongoClient(ip, port), "test_db");
    }

    @After
    public void clean() {
        mongodExecutable.stop();
    }

    @Test
    public void test() throws Exception {
        // given
        DBObject objectToSave = BasicDBObjectBuilder.start()
                .add("key", "value")
                .get();

        // when
        mongoTemplate.save(objectToSave, "collection");

        // then
        Assertions.assertThat(mongoTemplate.findAll(DBObject.class, "collection")).extracting("key")
                .containsOnly("value");
    }

    @Test
    public void testCount() {

        // given
        String id = "SCHOOL_OF_ROCK";

        Student student = new Student();
        student.setId(id);
        student.setName("hongildong");
        student.setGrade(2);
        student.setAge(18);
        student.setHobby(Arrays.asList("축구"));

        mongoTemplate.save(student);


        // when
        long result = mongoTemplate.count(new Query(), Student.class);


        // then
        Assert.assertEquals(1, result);
    }

    @Test
    public void testFind() {

        // given
        String id = "SCHOOL_OF_ROCK";

        Student student = new Student();
        student.setId(id);
        student.setName("hongildong");
        student.setGrade(2);
        student.setAge(18);
        student.setHobby(Arrays.asList("축구"));

        mongoTemplate.save(student);


        // when
        List<Student> result = mongoTemplate.find(new Query(), Student.class);

        // then
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void testFindOne() {

        // given
        String id = "SCHOOL_OF_ROCK";

        Student student = new Student();
        student.setId(id);
        student.setName("hongildong");
        student.setGrade(2);
        student.setAge(18);
        student.setHobby(Arrays.asList("축구"));

        mongoTemplate.save(student);


        // when
        Criteria criteria = new Criteria("id");
        criteria.is(id);

        Student result = mongoTemplate.findOne(new Query(criteria), Student.class);

        // then
        Assert.assertNotNull(result);
        Assert.assertEquals(id, result.getId());
    }

    @Test
    public void testSave() {

        // given
        String id = "SCHOOL_OF_ROCK";

        Student student = new Student();
        student.setId(id);
        student.setName("hongildong");
        student.setGrade(2);
        student.setAge(18);
        student.setHobby(Arrays.asList("축구"));

        // when
        Student result = mongoTemplate.save(student);

        // then
        Assert.assertNotNull(result);
        Assert.assertEquals(id, result.getId());
    }

    @Test
    public void testUpdate() {

        // given
        String id = "SCHOOL_OF_ROCK";

        Student student = new Student();
        student.setId(id);
        student.setName("hongildong");
        student.setGrade(2);
        student.setAge(18);
        student.setHobby(Arrays.asList("축구"));

        mongoTemplate.save(student);


        // when
        Criteria criteria = new Criteria("id");
        criteria.is(id);

        Update update = new Update();
        update.set("name", student.getName());
        update.set("grade", 3);
        update.set("age", 19);
        update.set("hobby", student.getHobby());

        mongoTemplate.updateFirst(new Query(criteria), update, Student.class);


        // then
        Student result = mongoTemplate.findOne(new Query(criteria), Student.class);

        Assert.assertNotNull(result);
        Assert.assertEquals(id, result.getId());
        Assert.assertEquals(19, result.getAge());
    }

    @Test
    public void testDelete() {

        // given
        String id = "SCHOOL_OF_ROCK";

        Student student = new Student();
        student.setId(id);
        student.setName("hongildong");
        student.setGrade(2);
        student.setAge(18);
        student.setHobby(Arrays.asList("축구"));

        mongoTemplate.save(student);


        // when
        Criteria criteria = new Criteria("id");
        criteria.is(id);
        Student result = mongoTemplate.findAndRemove(new Query(criteria), Student.class);

        // then
        Assert.assertNotNull(result);
        Assert.assertEquals(id, result.getId());
    }



}
