package icu.kandx.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import icu.kandx.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 发布订阅模式-生产者
 * @Author Shaodi.kou
 * @Date 2021/6/18 14:06
 */
public class Producer {
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtil.getChannel();
        /* 声明一个fanout类型的交换机
         * args:
         *       1. 交换机名称
         *       2. 交换机类型 */
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        Map<String, String> msgMap = new HashMap<>();
        msgMap.put("quick.orange.rabbit", "被C1C2接收到");
        msgMap.put("lazy.orange.elephant", "被C1C2接收到");
        msgMap.put("quick.orange.fox", "被C1接收到");
        msgMap.put("lazy.brown.fox", "被C2接收到");
        msgMap.put("lazy.pink.rabbit", "被C2接收到");
        msgMap.put("quick.brown.fox", "被丢弃");
        msgMap.put("quick.orange.male.rabbit", "被丢弃");
        msgMap.put("lazy.orange.male.rabbit", "被C2接收到");

        for (Map.Entry<String, String> msgEntry : msgMap.entrySet()) {
            channel.basicPublish(EXCHANGE_NAME, msgEntry.getKey(), null, msgEntry.getValue().getBytes());
            System.out.println("生产者发送消息: " + msgEntry.getValue());
        }
    }
}
