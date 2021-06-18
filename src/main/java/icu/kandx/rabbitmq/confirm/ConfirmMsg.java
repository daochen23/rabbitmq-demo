package icu.kandx.rabbitmq.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import icu.kandx.rabbitmq.utils.RabbitMqUtil;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 消息发布确认
 * @Author Shaodi.kou
 * @Date 2021/6/18 10:20
 */
public class ConfirmMsg {

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        // 单独确认消息,耗时: 620
        //publishMsgIndividually();

        // 批量确认消息,耗时: 148
        //publishMsgBatch();

        // 异步确认消息,耗时: 68
        publishMsgAsync();
    }

    public static void publishMsgIndividually() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        // 声明一个队列
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        long start = System.currentTimeMillis();
        // 批量发送消息
        for (int i = 0; i <= 1000; i++) {
            String msg = i + "";
            channel.basicPublish("", queueName, null, msg.getBytes());
            // 单个消息发布完后就进行确认
            boolean b = channel.waitForConfirms();
            if (b) {
                System.out.println("消息发送成功");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("单独确认消息,耗时: " + (end - start));
    }

    public static void publishMsgBatch() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        // 声明一个队列
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        long start = System.currentTimeMillis();
        // 批量发送消息
        // 批量确认的条数
        int batchSize = 100;
        for (int i = 0; i <= 1000; i++) {
            String msg = i + "";
            channel.basicPublish("", queueName, null, msg.getBytes());
            if (i % 100 == 0) {
                // 单个消息发布完后就进行确认
                boolean b = channel.waitForConfirms();
                if (b) {
                    System.out.println("消息发送成功");
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("批量确认消息,耗时: " + (end - start));
    }

    public static void publishMsgAsync() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        // 声明一个队列
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();

        // 存储消息
        ConcurrentSkipListMap<Long, String> concurrentSkipListMap = new ConcurrentSkipListMap<>();

        long start = System.currentTimeMillis();

        // 消息监听器,监听那些消息成功那些消息失败
        // 消息确认成功
        ConfirmCallback ackCallback = (l, b) -> {
            // 删除已经确认的消息
            // 如果是批量确认消息
            if (b) {
                ConcurrentNavigableMap<Long, String> batchMsg = concurrentSkipListMap.headMap(l);
                batchMsg.clear();
            }else {
                concurrentSkipListMap.remove(l);
            }
            System.out.println("确认的消息: " + l);
        };
        // 消息确认失败
        ConfirmCallback nackCallback = (l, b) -> {
            String nAckMsg = concurrentSkipListMap.get(l);
            System.out.println("未确认的消息编号: " + l);
            System.out.println("未确认的消息: " + nAckMsg);
        };
        channel.addConfirmListener(ackCallback, nackCallback);

        // 批量发送消息
        for (int i = 0; i <= 1000; i++) {
            String msg = i + "";
            channel.basicPublish("", queueName, null, msg.getBytes());
            // 记录所有要发送的消息
            concurrentSkipListMap.put(channel.getNextPublishSeqNo(), msg);
        }

        long end = System.currentTimeMillis();
        System.out.println("批量确认消息,耗时: " + (end - start));
    }
}
