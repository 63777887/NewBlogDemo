package com.example.myblog;

import com.example.myblog.bean.User;
import com.example.myblog.dao.UserDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MyBlogApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MyBlogApplication.class, args);
        UserDao userDao = context.getBean(UserDao.class);
        User user=userDao.selectUserByName("用户9");
        System.out.println(userDao.selectUserByName("用户9").toString());

    }

}
