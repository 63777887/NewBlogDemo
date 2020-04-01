package com.example.myblog.service;

import com.example.myblog.bean.Blog;
import com.example.myblog.dao.BlogDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BlogService {

    @Autowired
    private BlogDao blogDao;

    public Blog getBlogById(Integer id){
        return blogDao.findBlogById(id);
    }
    public Blog getBlogInfoById(Integer id){
        return blogDao.findBlogInfoByBlogId(id);
    }

    public PageInfo<Blog> pageUserBlog(String username, Integer page, Integer size){
        PageHelper.startPage(page,size);
        List<Blog> allBlogs= blogDao.findBlogsByUserName(username);
        return new PageInfo<Blog>(allBlogs);
    }
}
