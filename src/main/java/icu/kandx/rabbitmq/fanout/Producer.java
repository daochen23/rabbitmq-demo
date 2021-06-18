package icu.kandx.rabbitmq.fanout;

import com.rabbitmq.client.Channel;
import icu.kandx.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 发布订阅模式-生产者
 * @Author Shaodi.kou
 * @Date 2021/6/18 14:06
 */
public class Producer {
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtil.getChannel();
        /* 声明一个fanout类型的交换机
         * args:
         *       1. 交换机名称
         *       2. 交换机类型 */
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String msg = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes());
            System.out.println("生产者发送消息");
        }
    }
}
