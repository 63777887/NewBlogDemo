package com.example.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public void storeToken(String k,String v,Integer s){
        stringRedisTemplate.opsForValue().set(k, v, s, TimeUnit.SECONDS);
    }
    public void tokenCount(String k,String v,Integer s){
        stringRedisTemplate.opsForValue().set(k+"_code", v, s, TimeUnit.SECONDS);
    }
    public void tokenCountIncr(String name){
        stringRedisTemplate.opsForValue().increment(name);
    }

    public String getUsernameByToken(String token){
        return stringRedisTemplate.opsForValue().get(token);
    }
    public String getTokenByName(String name){
        return stringRedisTemplate.opsForValue().get(name);
    }
    public String getCount(String name){
        return stringRedisTemplate.opsForValue().get(name+"_code");
    }

}
