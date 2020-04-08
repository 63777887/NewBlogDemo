package com.example.myblog.dao;

import com.example.myblog.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.Cacheable;

@Mapper
public interface UserDao {
     User findUserByName(String name);

    void updateAvatar(Integer id, String avatarPath);

    void updatePasswd(String passwd, Integer id);

    String findUserNameByEmail(String email);
}
