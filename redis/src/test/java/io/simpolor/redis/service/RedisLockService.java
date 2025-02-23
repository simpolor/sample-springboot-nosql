package io.simpolor.redis.service;

import io.simpolor.redis.RedisApplication;
import io.simpolor.redis.domain.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = RedisApplication.class)
public class RedisLockService {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void test() throws InterruptedException {

        Runnable runnable1 = () -> lock(Student.builder().key("abcd").name("hongildong").age(19).grade(3).build());
        Runnable runnable2 = () -> lock(Student.builder().key("abcd").name("hongildong").age(18).grade(2).build());
        Runnable runnable3 = () -> lock(Student.builder().key("abcd").name("hongildong").age(17).grade(1).build());
        Runnable runnable4 = () -> lock(Student.builder().key("efgh").name("parsky").age(18).grade(2).build());
        Runnable runnable5 = () -> lock(Student.builder().key("efgh").name("parksy").age(17).grade(1).build());
        Runnable runnable6 = () -> lock(Student.builder().key("ijkl").name("sun").age(18).grade(2).build());

        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);
        Thread thread3 = new Thread(runnable3);
        Thread thread4 = new Thread(runnable4);
        Thread thread5 = new Thread(runnable5);
        Thread thread6 = new Thread(runnable6);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();


        TimeUnit.SECONDS.sleep(5);
    }

    private void lock(Student student) {
        String name = "LOCK_STUDENT:"+student.getKey();
        RLock locker = redissonClient.getLock(name);
        try {
            boolean acquired = locker.tryLock(3, 2, TimeUnit.SECONDS);
            if (!acquired) {
                throw new Exception("Acquired error");
            }

            System.out.println(">>>> Logic Process : "+student.getKey() +":"+student.getName()+":"+student.getGrade());

            locker.unlock();
            locker = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(locker != null ){
                locker.unlock();
            }

        }
    }
}
