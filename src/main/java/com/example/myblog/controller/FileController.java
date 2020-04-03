package com.example.myblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class FileController {


    public final ResourceLoader resourceLoader;

    String UPLOAD_DIR="/Users/edz/pic";

    Logger logger= LoggerFactory.getLogger(getClass());

    public FileController(@Autowired ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    @GetMapping("/upload")
    String showUploadPage(){
        return "upload";
    }

    @PostMapping("/upload")
    String doFileUpload(@RequestParam(value = "file") MultipartFile file,
                        @RequestParam(value = "name") String name) throws IOException {
        Files.copy(file.getInputStream(), Paths.get(UPLOAD_DIR,name));
        return "redirect:/picView/"+name;
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
