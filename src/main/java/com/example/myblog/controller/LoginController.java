package com.example.myblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLogin(){
        return "login";
    }
    @Autowired
    JdbcTemplate jdbcTemplate;

    @ResponseBody
    @GetMapping("/sql")
    public String showSql(){
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from blog");
        return maps.get(0).toString();
    }
}
