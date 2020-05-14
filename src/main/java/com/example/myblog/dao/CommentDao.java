package com.example.myblog.dao;

import com.example.myblog.bean.Comment;
import com.example.myblog.bean.CommentWithUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Mapper
public interface CommentDao {

    List<Comment> findCommentByBlogId(Integer id);

    List<CommentWithUser> getAllComment();
}
