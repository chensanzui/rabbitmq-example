package com.rabbitmq.example.springboot.direct;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author chenzhiyuan
 * @date 2020/3/16
 * @Description
 */
@Component
@RabbitListener(queues = "directQueue")
public class DirectReceiver {

    @RabbitHandler
    public void process(String message) {
        System.out.println("directReceiver消费者收到消息  : "+ message);
    }

}
