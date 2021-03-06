package com.rabbitmq.example.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author chenzhiyuan
 * @date 2020/3/16
 * @Description
 */
public class ConfirmListenerProducer {
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
            }
            channel.addConfirmListener(new ConfirmListener() {
                @Override
                public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println(String.format("已确认消息，标识：%d，多个消息：%b", deliveryTag, multiple));
                }

                @Override
                public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("未确认消息，标识：" + deliveryTag);
                }
            });
            long end = System.currentTimeMillis();
            System.out.println("发送"+max+"发送方确认模式消息，耗时为："+(end-start));
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        channel.close();
        connection.close();

    }
}
