package com.example.myblog.dao;

import com.example.myblog.bean.Blog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogDao {
    Blog findBlogById(Integer id);
    List<Blog> findBlogsByUserName(String username);
    Blog findBlogDetailByBlogId(Integer id);

//    void insertBlog(String title,String content,Integer userId);
    void insertBlog(Blog blog);
}
