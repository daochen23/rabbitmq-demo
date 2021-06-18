package icu.kandx.rabbitmq.dead_msg;

import com.rabbitmq.client.Channel;
import icu.kandx.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 死信队列-生产者
 * @Author Shaodi.kou
 * @Date 2021/6/18 16:04
 */
public class Producer {
    public static final String EXCHANGE_NAME = "normal_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtil.getChannel();
        /* 声明一个fanout类型的交换机
         * args:
         *       1. 交换机名称
         *       2. 交换机类型 */
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        // 设置消息接收时长TTL = 10s
        //AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();
        for (int i = 0; i < 10; i++) {
            String msg = i + "";
            channel.basicPublish(EXCHANGE_NAME, "zhangsan", null, msg.getBytes());
            System.out.println("生产者发送消息: " + msg);
        }
    }
}
