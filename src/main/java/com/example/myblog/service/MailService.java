package com.example.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class MailService {
    @Autowired
    JavaMailSenderImpl javaMailSender;

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    public void sendActiveMsg(String name){
        String msg=" .请点击如下链接进行激活---";
//        String url="http://localhost:8080/active?token=";
        String url="http://localhost:8080/active/"+name;
        long token = new Date().getTime();
//        url+=token;
//        msg+=url;
        msg="你已经注册半圆网络的blog，您的验证码是："+token+msg+url;
        redisService.storeToken(String.valueOf(token), name, 60);
        redisService.storeToken(name,String.valueOf(token),60);
        String email = userService.findUserByName(name).getEmail();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("63777887@qq.com");
        message.setTo("63777887@qq.com");
        message.setSubject("半圆网络账户激活邮件");
        message.setText(msg);
        javaMailSender.send(message);
    }
}
