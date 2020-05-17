package com.example.myblog.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CaptchaCode implements Serializable {

    private String code;

    private LocalDateTime expireTime;


    public CaptchaCode(String code, int expireAfterSeconds){
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireAfterSeconds);
    }

    public boolean isExpired(){
        return  LocalDateTime.now().isAfter(expireTime);
    }

    public String getCode() {
        return code;
    }
}
