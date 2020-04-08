package com.example.myblog.controller;

import com.example.myblog.bean.User;
import com.example.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String showLogin(@RequestParam(required = false) String next,
                            HttpSession session){
        session.removeAttribute("USER");
        return "login";
    }

//    @PostMapping("/login")
//    public String login(@RequestParam String username,
//                        @RequestParam String password,
//                        HttpSession session,
//                        Model model){
//        User user = userService.findUserByName(username);
//        if (user!=null && password.equals(user.getPassword())){
//            session.setAttribute("USER", user);
//            String nextUrl = (String) session.getAttribute("NEXT");
//            if (nextUrl!=null){
//                session.removeAttribute("NEXT");
//                return "redirect:".concat(nextUrl);
//            }else {
//                return "redirect:/admin/"+username;
//            }
//        }else {
//            return "redirect:/login";
//        }
//
//    }

    @PostMapping("/login/change-password")
    String changePassword(HttpSession session,
                          @RequestParam(value = "oldPasswd") String oldPassword,
                          @RequestParam(value = "newPasswd") String newPassword,
                          Model model
    ) {
        String username = ((User)session.getAttribute("USER")).getName();
        User user = userService.findUserByName(username);
        String oldInDB = user.getPassword();
        if (oldInDB.equals(oldPassword)) {
            //old password 正确
            user.setPassword(newPassword);
            userService.updatePasswd(newPassword, user.getId());
            model.addAttribute("message", "修改密码成功");
        } else {
            // 原始密码不正确,怎么处理??
            model.addAttribute("message", "原始密码不正确");
        }
        model.addAttribute("user", user);
        return "/admin";
    }

    @PostMapping("/logout")
    String logout(HttpSession session) {
        session.removeAttribute("USER");
        return "redirect:/";
    }
}
