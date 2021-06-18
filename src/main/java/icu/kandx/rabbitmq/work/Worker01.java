package icu.kandx.rabbitmq.work;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import icu.kandx.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 工作线程
 * @Author Shaodi.kou
 * @Date 2021/6/17 15:31
 */
public class Worker01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtil.getChannel();
        // 设置为不公平分发,能者多劳
        channel.basicQos(1);

        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            /*try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            System.out.println("接受到的消息: " + new String(message.getBody()));
        };

        // 取消消息的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消费消息被中断");
        };

        System.out.println("Worker02启动成功......");
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
