package com.rabbitmq.example.springboot.ack;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenzhiyuan
 * @date 2020/3/16
 * @Description
 */
@RestController
public class AckMessageController {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @GetMapping("/sendAcktMessage")
    public String sendDirectMessage() {
        String message = "spring boot ack message, hello!";
        //发送消息
        rabbitTemplate.convertAndSend("ackExchange", "ackRoutingKey", message);
        return "ok";

    }
}
