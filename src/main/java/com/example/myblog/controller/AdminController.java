package com.example.myblog.controller;

import com.example.myblog.bean.Blog;
import com.example.myblog.bean.User;
import com.example.myblog.service.BlogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {
    @Autowired
    private BlogService blogService;

    @GetMapping("/admin/{username}")
    public String showAdminHtml(
            @PathVariable String username,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            Model model,
            HttpSession session) {
        PageHelper.startPage(page.orElse(1),size.orElse(5));
        List<Blog> blogs = blogService.pageUserBlog(username);
        PageInfo<Blog> blogPageInfo = new PageInfo<>(blogs);
        User user=(User) session.getAttribute("USER");
        model.addAttribute("blogs", blogPageInfo);
        model.addAttribute("username", username);
        model.addAttribute(user);
        return "admin";
    }
}