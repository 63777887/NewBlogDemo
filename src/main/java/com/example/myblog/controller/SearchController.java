package com.example.myblog.controller;

import com.example.myblog.bean.*;
import com.example.myblog.service.BlogService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class SearchController {

    @Autowired
    BlogService blogService;

    @GetMapping("/search")
    String searchTitle(@RequestParam(value = "key", required = false) String keyword,
                       @RequestParam(value = "page", required = false,defaultValue = "1") Integer page,
                       @RequestParam(value = "size", required = false,defaultValue = "50") Integer size,
                       Model model) throws IOException, InstantiationException, IllegalAccessException {
        PageBean<BlogWithOutUser> blogs = blogService.searchBlog(keyword, page, size);
        model.addAttribute("blogs",blogs);
        model.addAttribute("keyword", keyword);
        return "search";
    }
}
