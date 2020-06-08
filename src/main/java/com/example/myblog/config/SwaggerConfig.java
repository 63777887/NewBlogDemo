package com.example.myblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

//        配置swagger的docket实例
    @Bean
    public Docket docket(Environment environment){
//        检测运行环境
        Profiles dev = Profiles.of("dev");
        boolean b = environment.acceptsProfiles(dev);
        return new Docket(DocumentationType.SWAGGER_2)
//                是否使用swagger
                .enable(true)
                .apiInfo(apiInfo())
//                扫描接口的方式
                .select().apis(RequestHandlerSelectors.basePackage("com.example.myblog.controller"))
//                扫描的路径
                .paths(PathSelectors.ant("/**"))
                .build();
    }
    private ApiInfo apiInfo(){
//        作者信息
        Contact contact = new Contact("季伟宽", "63777887@qq.com", "63777887@qq.com");
        return new ApiInfo("jwk的Api文档",
                "我的博客api",
                "1.0",
                "http://www.baidu.com",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }

}
