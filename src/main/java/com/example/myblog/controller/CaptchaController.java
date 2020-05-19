package com.example.myblog.controller;

import com.example.myblog.bean.CaptchaCode;
import com.example.myblog.utils.MyContants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
public class CaptchaController {

    @Resource
    DefaultKaptcha captchaProducer;

    @GetMapping("/kaptcha")
    public void kaptcha(HttpSession session, HttpServletResponse response) throws IOException {
        //设置请求头，固定写法
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        String capText = captchaProducer.createText();  //获取密码

        session.setAttribute(MyContants.CAPTCHA_SESSION_KEY,
                new CaptchaCode(capText,2*60));

        try (ServletOutputStream out = response.getOutputStream()) {
            BufferedImage image = captchaProducer.createImage(capText);
            ImageIO.write(image, "jpg", out);
            out.flush();
        }
    }
}
