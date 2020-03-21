package com.rabbitmq.example.exchange.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author chenzhiyuan
 * @date 2020/3/15
 * @Description
 */
public class Producer {

    private final static String EXCHANGE_NAME = "exchange_direct";

    private final static String ROUTING_KEY = "routingKey_direct";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //创建连接
        Connection connection = connectionFactory.newConnection();
        //创建管道
        Channel channel = connection.createChannel();
        //声明交换器
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String message = "Hello World";
        //发布消息
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, message.getBytes());
        System.out.println("发送消息：" + message);
        channel.close();
        connection.close();
    }
}
