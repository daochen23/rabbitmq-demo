package icu.kandx.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 创建工厂工具类
 * @Author Shaodi.kou
 * @Date 2021/6/17 15:32
 */
public class RabbitMqUtil {
    /**
     * 获取一个信道
     * @author Shaodi.kou
     * @date 2021/6/17 15:37
     * @return com.rabbitmq.client.Channel
     */
    public static Channel getChannel() throws IOException, TimeoutException {
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
        return channel;
    }
}