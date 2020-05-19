package com.example.myblog.bean;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString
public class BlogWithOutUser {
    private Integer id;
    private String title;
    private String content;
    private Date createdTime;
    private Integer userId;

}
