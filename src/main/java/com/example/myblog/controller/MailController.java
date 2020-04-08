package com.example.myblog.controller;

import com.example.myblog.service.MailService;
import com.example.myblog.service.RedisService;
import com.example.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class MailController {

    @Autowired
    private MailService mailService;

    @Autowired
    private RedisService redisService;

    @Autowired
    UserService userService;

//
@PostMapping("/mail/send-active-mail")
@ResponseBody
void sendActiveMail(HttpServletRequest request){
    String email = request.getParameter("email");
//    System.out.println("email"+email);
    String name = userService.findUserNameByEmail(email);
//    System.out.println(name);
    String count = redisService.getCount(name);
    if (count==null){
        redisService.tokenCount(name,"1",24*60*60);
        mailService.sendActiveMsg(name);
//            return "邮件已发送";
    }else if (Integer.parseInt(count)<=2){
        redisService.tokenCountIncr(name);
        mailService.sendActiveMsg(name);
//            return "邮件已发送";
    }else {
//            return "3次已经用完";
    }

}

    @GetMapping("/active/{name}")
    String activeAccount(@PathVariable("name") String name,
                         Model model){

        model.addAttribute("name", name);


        return "send";
    }

    @PostMapping("/active/{name}")
    @ResponseBody
    String check(@RequestParam("code") String code,
                 @PathVariable("name") String name){

//        System.out.println(name);
        String token = redisService.getTokenByName(name);
//        System.out.println(token);
//        System.out.println(code);
        if (code.equals(token)){
            return "激活成功";
        }else {
            return "激活失败";
        }

    }

//    @GetMapping("/send")
//    String showSend(){
//        return "send";
//    }


}
