package com.example.myblog.controller;

import com.example.myblog.bean.Blog;
import com.example.myblog.bean.User;
import com.example.myblog.service.BlogService;
import com.example.myblog.service.CommentService;
import com.example.myblog.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class BlogController {

    @Autowired
    BlogService blogService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;



    @GetMapping("blogger/{username}")
    public String showBlogsByUserName(@PathVariable("username") String username,
                                      @RequestParam Optional<Integer> page,
                                      @RequestParam Optional<Integer> size,
                                      Model model){
//        User user = userService.findUserByName(username);
        PageInfo<Blog> pageInfo = blogService.pageUserBlog(username, page.orElse(1), size.orElse(2));
        User user;
        List<Blog> list = pageInfo.getList();
        if (list!=null) {
            user = list.get(0).getAuthor();
        }else {
            user=userService.findUserByName(username);
        }
        model.addAttribute("user",user);
        model.addAttribute("blogs",pageInfo);
        return "list";
    }





}
