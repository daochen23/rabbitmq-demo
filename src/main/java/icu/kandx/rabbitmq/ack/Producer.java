package icu.kandx.rabbitmq.ack;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import icu.kandx.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 应答模式测试--生产者
 * @Author Shaodi.kou
 * @Date 2021/6/17 16:38
 */
public class Producer {
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtil.getChannel();
        // 开启消息发布确认模式
        channel.confirmSelect();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String msg = scanner.next();
            // 设置消息持久化 MessageProperties.PERSISTENT_TEXT_PLAIN
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发送消息: " + msg);
        }
    }
}
