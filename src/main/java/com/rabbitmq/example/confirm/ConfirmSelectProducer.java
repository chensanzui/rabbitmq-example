package com.rabbitmq.example.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author chenzhiyuan
 * @date 2020/3/16
 * @Description
 */
public class ConfirmSelectProducer {
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
        String exchangeName = "exchange_direct";
        channel.exchangeDeclare(exchangeName, "direct");
        String message = "Hello World";
        //定义routingKey
        String routingKey = "routingKey_direct";

        try {
            long start=System.currentTimeMillis();
            int max = 10000;
            //开启发送方确认模式
            channel.confirmSelect();
            for(int i=0;i<max;i++) {
                //发布消息
                channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
                if(channel.waitForConfirms()) {
                    System.out.println("发送消息：" + message);
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("发送"+max+"发送方确认模式消息，耗时为："+(end-start));

        } catch (Exception e) {
            e.printStackTrace();
        }
        channel.close();
        connection.close();

    }
}
