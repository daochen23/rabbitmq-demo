package icu.kandx.rabbitmq.work;

import com.rabbitmq.client.Channel;
import icu.kandx.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 生产者
 * @Author Shaodi.kou
 * @Date 2021/6/17 15:54
 */
public class Producer {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtil.getChannel();

        /* 将信道连接到队列上
         *  args:
         *       1. queue 队列名称
         *       2. durable: 队列是否持久化
         *       3. 队列是否可以被多个消费者消费
         *       4. 是否自动删除,当最后一个消费者消费完后队列是否自动删除
         *       5. 其它参数*/
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入消息:");
        while (scanner.hasNext()) {
            String msg = scanner.next();
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            System.out.println("消息发送完成: " + msg);
        }
    }
}
