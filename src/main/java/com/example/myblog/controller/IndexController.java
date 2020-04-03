package com.example.myblog.controller;


import com.example.myblog.bean.Blog;
import com.example.myblog.service.BlogService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class IndexController {

    @Autowired
    BlogService blogService;

    @GetMapping({"/","/index"})
    String getIndex(@RequestParam Optional<Integer> page,
                    @RequestParam Optional<Integer> size,Model model){
        PageInfo<Blog> allBlog = blogService.getAllBlog(page.orElse(1), size.orElse(10));
        model.addAttribute("blogs", allBlog);
        return "index";
    }
}
