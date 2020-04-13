package com.example.myblog.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqListenerService {

//    @RabbitListener(queues = "msg_queue")
    public void receive(String s){
        System.out.println("监听到了消息。。。"+s);
    }
}
