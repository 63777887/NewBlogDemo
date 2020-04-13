package com.example.myblog.component;

import com.example.myblog.bean.AjaxResponse;
import com.example.myblog.bean.CustomException;
import com.example.myblog.bean.CustomExceptionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Value("${spring.security.loginType}")
    private String loginType;

    //字符串转换成json工具
    private static ObjectMapper objectMapper= new ObjectMapper();
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errorMsg = "用户名或者密码输入错误!";

        if(exception instanceof SessionAuthenticationException){
            errorMsg = exception.getMessage();
        }

        if (loginType.equals("JSON")){
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(
                    AjaxResponse.error(new CustomException(
                            CustomExceptionType.USER_INPUT_ERROR,
                            errorMsg)
            )));
        }else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }


}
