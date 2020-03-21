package com.rabbitmq.example.springboot.ack;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenzhiyuan
 * @date 2020/3/16
 * @Description
 */
@Configuration
public class AckReceiverConfig {
    @Bean
    public Queue ackQueue(){
        return new Queue("ackQueue",true);
    }
    @Bean
    public DirectExchange ackExchange(){
        return new DirectExchange("ackExchange");
    }

    @Bean
    public Binding ackBindingDirect() {
        return BindingBuilder.bind(ackQueue()).to(ackExchange()).with("ackRoutingKey");
    }

    @Autowired
    private CachingConnectionFactory connectionFactory;
    @Autowired
    private AckReceiver ackReceiver;


    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(1);
        // RabbitMQ默认是自动确认，这里改为手动确认消息
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setQueues(ackQueue());
        container.setMessageListener(ackReceiver);
        return container;

    }
}
