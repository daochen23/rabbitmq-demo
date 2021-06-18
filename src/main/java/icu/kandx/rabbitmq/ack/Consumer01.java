package icu.kandx.rabbitmq.ack;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import icu.kandx.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 手动应答模式--消费者01
 * @Author Shaodi.kou
 * @Date 2021/6/17 16:42
 */
public class Consumer01 {
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtil.getChannel();
        System.out.println("C1处理消息较快");
        // 当设置消费者信道的Qos为1时,就是开启不公平模式,也就是能者多劳
        //channel.basicQos(1);
        // 当设置消费者信道的Qos大于1时,就是开启预期值,无论消费者消费的快慢信道始终都会消费设置的预期值的条数
        channel.basicQos(2);
        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(new String(message.getBody()));
            /* 处理完消息后开始应答
            * args:
            *       1. 消息的标记 tag
            *       2. 是否批量应答 */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        // 取消消息的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消费消息被中断");
        };

        // 手动应答
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }
}
