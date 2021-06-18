package icu.kandx.rabbitmq.fanout;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import icu.kandx.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 发布订阅模式-消费者01
 * @Author Shaodi.kou
 * @Date 2021/6/18 14:07
 */
public class Consumer02 {
    // 交换机名称
    public static final String EXCHANGE_NAME = "logs";
    public static final String QUEUE_NAME01 = "queue02";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtil.getChannel();
        /* 声明一个fanout类型的交换机
        * args:
        *       1. 交换机名称
        *       2. 交换机类型 */
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        // 声明一个队列
        channel.queueDeclare(QUEUE_NAME01, true, false, false, null);

        /* 队列绑定交换机
        * args:
        *       1. 队列名称
        *       2. 交换机名称
        *       3. RoutingKey 路由键 */
        channel.queueBind(QUEUE_NAME01, EXCHANGE_NAME, "");

        System.out.println("C2等待接收消息......");

        // 接收到消息的回调
        DeliverCallback deliverCallback = (s, msg) -> {
            System.out.println("接收到的消息: " + new String(msg.getBody()));
        };
        // 消息取消的回调
        CancelCallback cancelCallback = tag -> {
            System.out.println("消息取消的回调");
        };
        channel.basicConsume(QUEUE_NAME01, deliverCallback, cancelCallback);
    }
}
