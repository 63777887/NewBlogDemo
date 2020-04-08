package com.example.myblog.service;

import com.example.myblog.bean.Blog;
import com.example.myblog.dao.BlogDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class BlogService {

    @Autowired
    private BlogDao blogDao;


//    @Cacheable(value = "blog",key = "#id")
    public Blog getBlogInfoById(Integer id){
        return blogDao.findBlogDetailByBlogId(id);
    }

//    @Cacheable(value = "blog")
    public PageInfo<Blog> pageUserBlog(String username, Integer page, Integer size){
        PageHelper.startPage(page,size);
       return new PageInfo<Blog>(blogDao.findBlogsByUserName(username));
    }

    public void createBlog(Blog blog){
//        blogDao.insertBlog(blog.getTitle(), blog.getContent(), blog.getAuthor().getId());
        blogDao.insertBlog(blog);
    }

    public void deleteBlogById(Integer bolgId) {
        blogDao.deleteBlogById(bolgId);
    }

//    @CachePut(value = "blog",key = "#id")
    public Blog putBlog(Blog blog,Integer id) {
        blogDao.putBlog(blog,id);
        return blogDao.findBlogDetailByBlogId(id);
    }
    public PageInfo<Blog> getAllBlog(Integer page, Integer size){
        PageHelper.startPage(page,size);
        return new PageInfo<Blog>(blogDao.getAllBlog());
    }



}
