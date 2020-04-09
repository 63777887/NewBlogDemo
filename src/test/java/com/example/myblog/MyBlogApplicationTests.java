package com.example.myblog;

import com.example.myblog.service.RedisService;
import com.example.myblog.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
class MyBlogApplicationTests {
    @Autowired
    DataSource dataSource;

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    AmqpAdmin amqpAdmin;



    @Test
    void contextLoads() {
    }

    @Test
    public void test01() throws SQLException {
        System.out.println(dataSource.getClass());
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
        connection.close();

    }
    @Test
    void test02(){
        System.out.println(redisService.getCount("aa"));
    }
    @Test
    public void test03(){
//        amqpAdmin.declareBinding(new Binding("atguigu", Binding.DestinationType.QUEUE, "exchange.direct","amqp.haha",null ));
        amqpAdmin.declareBinding(new Binding("msg_queue",Binding.DestinationType.QUEUE,"msg_exchange","msg_queue",null));
    }

}
