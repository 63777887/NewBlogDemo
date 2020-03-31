package com.example.myblog.service;

import com.example.myblog.bean.User;
import com.example.myblog.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User findUserByName(String name) {
        return userDao.findUserByName(name);
    }

}
