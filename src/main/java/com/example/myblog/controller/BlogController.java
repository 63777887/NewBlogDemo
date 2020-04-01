package com.example.myblog.controller;

import com.example.myblog.bean.Blog;
import com.example.myblog.bean.User;
import com.example.myblog.service.BlogService;
import com.example.myblog.service.CommentService;
import com.example.myblog.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @Cacheable
    @GetMapping("blogger/{username}")
    public String showBlogsByUserName(@PathVariable("username") String username,
                                      @RequestParam Optional<Integer> page,
                                      @RequestParam Optional<Integer> size,
                                      Model model){
        PageHelper.startPage(page.orElse(1),size.orElse(2));
//        User user = userService.findUserByName(username);
        List<Blog> blogs = blogService.pageUserBlog(username);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogs);
        User user = blogs.get(0).getAuthor();
        model.addAttribute("user",user);
        model.addAttribute("blogs",pageInfo);
        return "list";
    }

    @GetMapping("/blog/{id}")
    public String showBlogById(@PathVariable("id") Integer id,
                        Model model){
        Blog blog = blogService.getBlogInfoById(id);
//        Blog blog = blogService.getBlogById(id);
//        List<Comment> comments = commentService.findCommentByBlogId(id);
        model.addAttribute("blog",blog);
//        model.addAttribute("comments",comments);
        return "item";
    }

    @GetMapping("/blog/create")
    public String showCreate(){
        return "create";
    }


    @PostMapping("/blog/create")
    public String createBlog(Blog blog,
                             Model model){
        User aa = userService.findUserByName("aa");

        blog.setAuthor(aa);
//        model.addAttribute("user",aa);
        blogService.createBlog(blog);
        System.out.println(blog.getId());
        return "redirect:/blog/"+blog.getId();
    }

}
