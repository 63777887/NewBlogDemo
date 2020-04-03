package com.example.myblog.dao;

import com.example.myblog.bean.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Mapper
public interface BlogDao {

    List<Blog> findBlogsByUserName(String username);
    Blog findBlogDetailByBlogId(Integer id);

//    void insertBlog(String title,String content,Integer userId);
    void insertBlog(Blog blog);

    void deleteBlogById(Integer blogId);

    void putBlog(Blog blog, Integer id);
    List<Blog> getAllBlog();
}
