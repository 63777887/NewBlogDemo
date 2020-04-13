package com.example.myblog.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyExpiredSessionStrategy implements SessionInformationExpiredStrategy {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        System.out.println("进到了session过期处理");
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "您的账户已经在其他地方登陆，被迫下线");
        event.getResponse().setContentType("application/json;charset=UTF-8");
        event.getResponse().getWriter().write(
                objectMapper.writeValueAsString(map)
        );
    }
}
