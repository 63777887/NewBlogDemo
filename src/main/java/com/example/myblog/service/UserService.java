package com.example.myblog.service;

import com.example.myblog.bean.User;
import com.example.myblog.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
public class UserService {


    @Autowired
    private UserDao userDao;


//    @Cacheable(value = "user")
    public User findUserByName(String name) {
        return userDao.findUserByName(name);
    }

    public void updateAvatarById(Integer id, String avatarPath) {
        userDao.updateAvatar(id, avatarPath);
    };

    public void updatePasswd(String newPassword, Integer id) {
        userDao.updatePasswd(newPassword,id);
    }

    public String findUserNameByEmail(String email) {
        return userDao.findUserNameByEmail(email);
    }

}
