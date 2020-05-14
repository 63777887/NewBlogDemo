package com.example.myblog.bean;

import lombok.Data;

import java.util.Date;

@Data
public class CommentWithUser {
    private Integer id;
    private Date createdTime;
    private String content;
    private Integer userId;
    private Integer blogId;
}
