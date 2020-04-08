package com.example.myblog;

import com.example.myblog.service.RedisService;
import com.example.myblog.service.UserService;
import org.junit.jupiter.api.Test;
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
}
