package com.rabbitmq.example.springboot.direct;

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
public class DirectSendMessageController {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @GetMapping("/sendDirectMessage")
    public String sendDirectMessage() {
        String message = "spring boot message, hello!";
        //发送消息
        rabbitTemplate.convertAndSend("directExchange", "directRoutingKey", message);
        return "ok";

    }
}
