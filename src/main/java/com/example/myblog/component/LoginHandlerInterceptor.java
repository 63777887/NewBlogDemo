package com.example.myblog.component;


import com.example.myblog.bean.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("USER");
        if (user==null){
//            request.getRequestDispatcher("/login?next=".concat(request.getRequestURI())).forward(request, response);
            String uri = request.getRequestURI();
            request.getSession().setAttribute("NEXT", uri);
            response.sendRedirect("/login?next=".concat(uri));
            return false;
        }else {
        return true;
        }
    }
}
