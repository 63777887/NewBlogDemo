package com.example.myblog;

import com.example.myblog.bean.User;
import com.example.myblog.dao.BlogDao;
import com.example.myblog.dao.UserDao;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MyBlogApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MyBlogApplication.class, args);
        UserDao userDao = context.getBean(UserDao.class);
        User user=userDao.findUserByName("用户9");
        System.out.println(userDao.findUserByName("用户9").toString());
//        BlogDao bean = context.getBean(BlogDao.class);
//        System.out.println(bean.findBlogById(202));

    }

}
