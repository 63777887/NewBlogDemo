package com.example.myblog.controller;

import com.example.myblog.bean.Blog;
import com.example.myblog.bean.User;
import com.example.myblog.service.BlogService;
import com.example.myblog.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    String showAdminPage(Principal principal, Model model) {
        String userName = principal.getName();
        User user = userService.findUserByName(userName);
        model.addAttribute("user", user);
        return "admin";
    }

    @GetMapping("/blogs")
    public String showAdminBlogs(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            Principal principal,
            Model model,
            HttpSession session) {
//        User user=(User) session.getAttribute("USER");
        String name = principal.getName();
        User user = userService.findUserByName(name);
        PageInfo<Blog> blogPageInfo = blogService.pageUserBlog(name, page.orElse(1), size.orElse(10));
        model.addAttribute("blogs", blogPageInfo);
        model.addAttribute("username", name);
        model.addAttribute(user);
        return "admin-blogs";
    }
//    @GetMapping("/admin")
//    public String showAdminHtml(
//            Model model,
//            HttpSession session) {
//        User user=(User) session.getAttribute("USER");
//        model.addAttribute("user", user);
//        return "admin";
//    }

    @GetMapping("/blog/create")
    public String showCreate(HttpSession session,
                             HttpServletRequest request){
        User user = (User) session.getAttribute("USER");
//        if (user!=null){
        return "create";
//        }else {
//            return "redirect:/login?next=".concat(request.getRequestURI());
//        }
    }


    @PostMapping("/blog/create")
    public String createBlog(Blog blog,
                             Model model){

        User aa = userService.findUserByName("aa");

        blog.setAuthor(aa);
//        model.addAttribute("user",aa);
        blogService.createBlog(blog);
        System.out.println(blog.getId());
        return "redirect:/admin/blog/"+blog.getId();
    }

    @DeleteMapping("/blogs/{blogId}")
    public String deleteBlog(@PathVariable("blogId") Integer id,
                             HttpSession session,
                             Principal principal){
        User user = userService.findUserByName(principal.getName());
        Blog blog = blogService.getBlogInfoById(id);
        if (user.getName().equals(blog.getAuthor().getName())) {
            blogService.deleteBlogById(id);
        }
        return "redirect:/admin/blogs";
    }

    @GetMapping("/blogs/{blogId}/edit")
    public String showEdit(@PathVariable("blogId") Integer id,
                           Model model){
        Blog blog = blogService.getBlogInfoById(id);
        model.addAttribute("blog",blog);
        return "edit";
    }

    @PutMapping("/blogs/{blogId}/edit")
    public String putBlog(@PathVariable("blogId") Integer id,
                          Blog blog){
        blogService.putBlog(blog,id);
        return "redirect:/admin/blogs";
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
}