package com.example.myblog.bean;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    private Integer id;
    private Date createdTime;
    private String content;
    private User commenter;
    private Integer blogId;
}
