package com.example.myblog.service;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqService {

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void addDirect(String name){
        amqpAdmin.declareExchange(new DirectExchange(name+"_exchange", true, false));

        amqpAdmin.declareQueue(new Queue(name+"_queue",true));

        amqpAdmin.declareBinding(new Binding(name+"_queue", Binding.DestinationType.QUEUE, name+"_exchange",name+"_queue",null ));
    }


    public void addMsg(String massage,String name){
        rabbitTemplate.convertAndSend(name+"_exchange",name+"_queue",massage);
    }




}
