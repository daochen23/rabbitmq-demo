package icu.kandx.rabbitmq.dead_msg;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import icu.kandx.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 死信队列-消费者
 * @Author Shaodi.kou
 * @Date 2021/6/18 15:50
 */
public class Consumer01 {
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DEAD_EXCHANGE = "dead_exchange";
    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtil.getChannel();
        // 声明交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 声明队列
        Map<String, Object> argsMap = new HashMap<>();
        // 设置正常队列对应的死信交换机
        argsMap.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 设置死信RoutingKey
        argsMap.put("x-dead-letter-routing-key", "lisi");
        // 设置队列长队为6,超过的消息会写到死信队列中
        //argsMap.put("x-max-length", 6);
        channel.queueDeclare(NORMAL_QUEUE, true, false, false, argsMap);

        // 声明死信队列
        channel.queueDeclare(DEAD_QUEUE, true, false, false, null);

        // 队列绑定交换机
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        System.out.println("C1等待接收消息......");

        // 接收到消息的回调
        DeliverCallback deliverCallback = (s, msg) -> {
            String str = new String(msg.getBody(), "UTF-8");
            if ("5".equals(str)) {
                // 拒绝消息
                channel.basicReject(msg.getEnvelope().getDeliveryTag(), false);
                System.out.println("消息: " + str + "被拒绝");
            }else {
                System.out.println("接收到的消息: " + str);
                channel.basicAck(msg.getEnvelope().getDeliveryTag(), false);
            }
        };
        // 消息取消的回调
        CancelCallback cancelCallback = tag -> {
            System.out.println("消息取消的回调");
        };
        // 手动应答
        channel.basicConsume(NORMAL_QUEUE,false, deliverCallback, cancelCallback);
    }
}
