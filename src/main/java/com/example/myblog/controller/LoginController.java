package com.example.myblog.controller;

import com.example.myblog.bean.User;
import com.example.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String showLogin(@RequestParam(required = false) String next,
                            HttpSession session){
        if (next!=null){
            session.setAttribute("NEXT_URI", next);
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session){
        User user = userService.findUserByName(username);
        if (user!=null && password.equals(user.getPassword())){
            session.setAttribute("USER", user);
            String nextUrl = (String) session.getAttribute("NEXT_URI");
            if (nextUrl.contains("next")){
                String [] uriSplit = nextUrl.split("/");
                String redirectUrl = "";
                for (String i: uriSplit) {
                    if (i.length() > 0)
                        redirectUrl += "/"+i;
                }
                return "redirect:"+redirectUrl;
            }else {
                return "redirect:/admin/"+username;
            }
        }else {
            return "redirect:/login";
        }

    }
    @Autowired
    JdbcTemplate jdbcTemplate;

    @ResponseBody
    @GetMapping("/sql")
    public String showSql(){
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from blog");
        return maps.get(0).toString();
    }
}
