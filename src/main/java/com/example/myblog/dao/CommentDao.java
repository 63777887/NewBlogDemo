package com.example.myblog.dao;

import com.example.myblog.bean.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentDao {
    public List<Comment> findCommentByBlogId(Integer id);
}
