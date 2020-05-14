package com.example.myblog.bean;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BlogResp {
    private Integer id;
    private String title;
    private String content;
    private Date createdTime;
    private Integer userId;
    private List<Comment> comments;
}
