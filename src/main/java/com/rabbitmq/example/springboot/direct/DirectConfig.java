package com.rabbitmq.example.springboot.direct;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenzhiyuan
 * @date 2020/3/16
 * @Description
 */
@Configuration
public class DirectConfig {

    @Bean
    public Queue directQueue(){
        return new Queue("directQueue",true);
    }
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("directExchange");
    }

    @Bean
    public Binding bindingDirect() {
        return BindingBuilder.bind(directQueue()).to(directExchange()).with("directRoutingKey");
    }

}
