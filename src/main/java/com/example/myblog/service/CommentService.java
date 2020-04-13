package com.example.myblog.service;

import com.example.myblog.bean.Comment;
import com.example.myblog.dao.CommentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentService {

    @Autowired
    CommentDao commentDao;


    public List<Comment> findCommentByBlogId(Integer id){
        List<Comment> comments = commentDao.findCommentByBlogId(id);
        return comments;
    }
}
