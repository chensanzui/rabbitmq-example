package com.rabbitmq.example.springboot.ack;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * @author chenzhiyuan
 * @date 2020/3/16
 * @Description
 */
@Component
@RabbitListener(queues = "ackQueue")
public class AckReceiver implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String msg = message.toString();
        System.out.println("接收到消息:"+msg);
        channel.basicAck(deliveryTag, true);
    }
}
