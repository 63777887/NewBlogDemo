package com.example.myblog.dao;

import com.example.myblog.bean.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    public User selectUserByName(String name);
}
