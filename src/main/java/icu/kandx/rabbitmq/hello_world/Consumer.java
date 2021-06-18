package icu.kandx.rabbitmq.hello_world;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 消费者
 * @Author Shaodi.kou
 * @Date 2021/6/17 11:25
 */
public class Consumer {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建一个RabbitMQ的链接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置RabbitMQ的服务ip
        factory.setHost("192.168.148.103");
        // 设置RabbitMQ的用户密码
        factory.setUsername("admin");
        factory.setPassword("admin");

        // 创建一个连接
        Connection connection = factory.newConnection();
        // 创建一个信道
        Channel channel = connection.createChannel();

        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };

        // 取消消息的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消费消息被中断");
        };

        /*
        * 消费者消费消息
        * args:
        *       1. 消费哪个队列
        *       2. 消费成功之后是否要自动应答
        *       3. 消费者成功消费的回调
        *       4. 消费者取消消费的回调 */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
