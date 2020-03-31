package com.example.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchController {

    @GetMapping("/search")
    @ResponseBody
    String searchTitle(@RequestParam(value = "key", required = false) String keyword){
        return "key is "+keyword;
    }
}
