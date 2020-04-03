package com.example.myblog.controller;

import com.example.myblog.bean.Blog;
import com.example.myblog.bean.User;
import com.example.myblog.service.BlogService;
import com.example.myblog.service.CommentService;
import com.example.myblog.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
        User user = pageInfo.getList().get(0).getAuthor();
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
        return "redirect:/blog/"+blog.getId();
    }

    @DeleteMapping("/blog/{blogId}")
    public String deleteBlog(@PathVariable("blogId") Integer id,
                             HttpSession session){
        Blog blog = blogService.getBlogInfoById(id);
        User user = (User) session.getAttribute("USER");
        if (user.getName().equals(blog.getAuthor().getName())) {
            blogService.deleteBlogById(id);
        }
        return "redirect:/admin/"+user.getName();
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
        return "redirect:/admin/aa";
    }

}
