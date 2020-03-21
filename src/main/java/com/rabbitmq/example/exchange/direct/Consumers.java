package com.rabbitmq.example.exchange.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * @author chenzhiyuan
 * @date 2020/3/15
 * @Description
 */
public class Consumers {
    private final static String QUEUE_NAME = "hello1";

    private final static String EXCHANGE_NAME = "exchange_direct";

    private final static String ROUTING_KEY = "routingKey_direct";

    public static void main(String[] args) throws IOException, TimeoutException {
        //1.消息的发后既忘特性
        //发后既忘模式是指接受者不知道消息的来源，如果想要指定消息的发送者，需要包含在发送内容里面，这点就像我们在信件里面注明自己的姓名一样，只有这样才能知道发送者是谁
        //2.消息收到未确认会怎么样？
        //如果应用程序接收了消息，因为bug忘记确认接收的话，消息在队列的状态会从“Ready”变为“Unacked”
        //如果消息收到却未确认，Rabbit将不会再给这个应用程序发送更多的消息了，这是因为Rabbit认为你没有准备好接收下一条消息。
        //此条消息会一直保持Unacked的状态，直到你确认了消息，或者断开与Rabbit的连接，Rabbit会自动把消息改完Ready状态，分发给其他订阅者。
        //当然你可以利用这一点，让你的程序延迟确认该消息，直到你的程序处理完相应的业务逻辑，这样可以有效的防治Rabbit给你过多的消息，导致程序崩溃。
        //3消息拒绝?
        //消息在确认之前，可以有两个选择：
        //选择1：断开与Rabbit的连接，这样Rabbit会重新把消息分派给另一个消费者；
        // 选择2：拒绝Rabbit发送的消息使用
        // channel.basicReject(long deliveryTag, boolean requeue)，参数1：消息的id；参数2：处理消息的方式，如果是true，Rabbib会重新分配这个消息给其他订阅者，如果设置成false的话，Rabbit会把消息发送到一个特殊的“死信”队列，用来存放被拒绝而不重新放入队列的消息。

        ExecutorService es = Executors.newFixedThreadPool(10);
        for(int i=0;i<10;i++){
            es.execute(new Consumer());
        }
    }
    public static class Consumer implements Runnable{

        @Override
        public void run() {
            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost("127.0.0.1");
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.exchangeDeclare(EXCHANGE_NAME, "direct");
                // 由RabbitMQ自行创建的临时队列,唯一且随消费者的中止而自动删除的队列
               // String queueName = channel.queueDeclare().getQueue();
                channel.queueDeclare(QUEUE_NAME,false,false,false,null);
                // 或声明创建持久队列
                // String queueName = ROUTING_KEY + ".queue";
                // channel.queueDeclare(queueName, false, false, true, null);

                // binding
                channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);


                System.out.println(" [*] 正在等待消息. 退出按 CTRL+C");

                com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        String message = new String(body, "UTF-8");
                        System.out.println(Thread.currentThread().getId()+" 接收消息：" + message + "'");
                        channel.basicAck(envelope.getDeliveryTag(),true);
                    }
                };
                channel.basicConsume(QUEUE_NAME, false, consumer);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}

