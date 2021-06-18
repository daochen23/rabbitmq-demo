package icu.kandx.rabbitmq.topic;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import icu.kandx.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 主题模式-消费者
 * @Author Shaodi.kou
 * @Date 2021/6/18 15:01
 */
public class Consumer02 {
    public static final String EXCHANGE_NAME = "topic_logs";
    public static final String QUEUE_NAME01 = "topic_q2";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtil.getChannel();
        // 声明一个topic类型的交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        // 声明一个队列
        channel.queueDeclare(QUEUE_NAME01, true, false, false, null);
        // 将队列绑定到交换机上
        channel.queueBind(QUEUE_NAME01, EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind(QUEUE_NAME01, EXCHANGE_NAME, "lazy.#");

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
