package com.example.myblog.controller;

import com.example.myblog.service.RabbitMqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RabbitMqController {

    @Autowired
    RabbitMqService rabbitMqService;

    @GetMapping("/adddirect/{name}")
    @ResponseBody
    public String addDirect(@PathVariable("name") String name){
        rabbitMqService.addDirect(name);
        return "加入成功";
    }
    @GetMapping("/adddirect/{name}/{massage}")
    @ResponseBody
    public String addMassage(@PathVariable("name") String name,
                             @PathVariable("massage") String massage){
        rabbitMqService.addMsg(massage,name);
        return "发送成功";
    }

}
