package com.example.myblog.controller;

import com.example.myblog.bean.Blog;
import com.example.myblog.bean.User;
import com.example.myblog.service.BlogService;
import com.example.myblog.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class BlogController {

    @Autowired
    BlogService blogService;

    @Autowired
    UserService userService;

    @GetMapping("blogger/{username}")
    public String showBlogsByUserName(@PathVariable("username") String username,
                                      Model model){
        User user = userService.findUserByName(username);
        PageInfo<Blog> blogs = blogService.pageUserBlog(username, 1, 10);
        model.addAttribute("user",user);
        model.addAttribute("blogs",blogs);
        return "list";
    }

    @GetMapping("/blog/{id}")
    public String showBlogById(@PathVariable("id") Integer id,
                        Model model){
        Blog blog = blogService.getBlogById(id);
        model.addAttribute("blog",blog);
        return "item";
    }

}
