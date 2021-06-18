package icu.kandx.rabbitmq.hello_world;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 生产者
 * @Author Shaodi.kou
 * @Date 2021/6/17 10:49
 */
public class Producer {
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

        /* 将信道连接到队列上
        *  args:
        *       1. queue 队列名称
        *       2. durable: 队列是否持久化
        *       3. 队列是否可以被多个消费者消费
        *       4. 是否自动删除,当最后一个消费者消费完后队列是否自动删除
        *       5. 其它参数*/
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        String msg = "hello world";
        /* 通过信道发送消息
        * args:
        *       1. 交换机名称: 这里使用的是默认的交换机
        *       2. 队列名称
        *       3. 路由的Key值
        *       4. 发送的消息 */
        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
        System.out.println("消息发送完毕");
    }
}
