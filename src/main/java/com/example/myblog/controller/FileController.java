package com.example.myblog.controller;

import com.example.myblog.bean.User;
import com.example.myblog.dao.UserDao;
import com.example.myblog.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileController {


    public final ResourceLoader resourceLoader;
    public UserService userService;

    String UPLOAD_DIR="/Users/edz/pic";

    Logger logger= LoggerFactory.getLogger(getClass());

    public FileController(@Autowired ResourceLoader resourceLoader,
                          @Autowired UserService userService) {
        this.resourceLoader = resourceLoader;
        this.userService=userService;
    }


//    @GetMapping("/upload")
//    String showUploadPage(){
//        return "upload";
//    }

    @PostMapping("/upload")
    String upload(@RequestParam(value = "file") MultipartFile file,
                  @RequestParam(value = "name") String fileName,
                  Model model,
                  HttpSession session
    ) throws IOException {
        // 把file存放为本地的一个文件
        Path avatarPath = Paths.get(UPLOAD_DIR, fileName);
        Files.copy(file.getInputStream(), avatarPath);
        // 将文件的路径保存到db里面
        User user = (User)session.getAttribute("USER");
        user.setAvatar(avatarPath.toString());
        userService.updateAvatarById(user.getId(), avatarPath.toString());
        model.addAttribute("user", user);
        return "/admin";
    }

    @GetMapping("/picView/{filename}")
    @ResponseBody
    ResponseEntity<?> getPic(@PathVariable(value = "filename") String name) {
        return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(UPLOAD_DIR, name)));
    }

    @GetMapping("/showPic")
    String showPic(@RequestParam(value = "name") String name,Model model){
        model.addAttribute("filename", name);
        return "picView";
    }

    @GetMapping("/avatar/{username}")
    @ResponseBody
    ResponseEntity<?> getAvatar(@PathVariable(value = "username") String name) {
        // 获取头像路径
        User user = userService.findUserByName(name);
        if (user.getAvatar() == null) {
            return null;
        }
        // 返回头像图片的内容
        return ResponseEntity.ok(
                resourceLoader.getResource("file:" + user.getAvatar() ) );
    }



//    @GetMapping("/upload")
//    String showUploadPage(){
//        return "upload";
//    }
//
//    @PostMapping("/upload")
//    String doFileUpload(@RequestParam MultipartFile file,
//                        @RequestParam String name) throws IOException {
//        logger.info("name is -"+name);
//        Files.copy(file.getInputStream(), Paths.get("/Users/edz/pic/"+name+".jpg"));
//        return "redirect:/picView/"+name;
//    }
//
//    @GetMapping("/picView/{name}")
//    String showPic(@PathVariable String name,
//                   Model model) throws UnsupportedEncodingException {
//        String tName=new String(name.getBytes(),"utf-8");
//        model.addAttribute("name",tName);
//        logger.info(tName);
//        return "picView";
//    }
}
