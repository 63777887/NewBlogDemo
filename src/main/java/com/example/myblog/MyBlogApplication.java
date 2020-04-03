package com.example.myblog;

import com.example.myblog.bean.User;
import com.example.myblog.dao.BlogDao;
import com.example.myblog.dao.CommentDao;
import com.example.myblog.dao.UserDao;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableCaching
public class MyBlogApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MyBlogApplication.class, args);
        UserDao userDao = context.getBean(UserDao.class);
        System.out.println(userDao.findUserByName("用户9").toString());
        BlogDao blogDao = context.getBean(BlogDao.class);
        System.out.println(blogDao.findBlogDetailByBlogId(209));
        System.out.println();
        CommentDao bean1 = context.getBean(CommentDao.class);
        System.out.println(bean1.findCommentByBlogId(209));

    }

}
