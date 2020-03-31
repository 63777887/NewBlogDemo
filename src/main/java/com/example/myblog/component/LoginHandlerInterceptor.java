package com.example.myblog.component;


import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("USER");
        if (user==null){
            request.getRequestDispatcher("/login").forward(request, response);
            return false;
        }else {
        return true;
        }
    }
}
