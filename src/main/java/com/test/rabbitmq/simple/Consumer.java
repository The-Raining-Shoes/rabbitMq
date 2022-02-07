package com.test.rabbitmq.simple;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static void main(String[] args) {
        // 所有的中间件技术都是基于TCP/IP协议的 只不过rabbitmq遵循的是amqp协议
        // ip port(必要)
        // 1.创建连接工程
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("139.155.181.217");
        connectionFactory.setPort(5673);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("/");
        // 5.准备消息内容
        // 6.发送消息给队列queue
        // 7.关闭连接
        // 8.关闭通道
        Channel channel = null;
        Connection connection = null;
        try {
            // 2.创建连接
            connection = connectionFactory.newConnection("生产者");
            // 3.创建连接获取通道
            channel = connection.createChannel();
            String queueName = "queue4";
            channel.basicConsume(queueName, true, (s, delivery) -> System.out.println("接收到的消息是" + new String(delivery.getBody(), StandardCharsets.UTF_8)),
                    a -> System.out.println("接受消息失败了" + a));
            System.out.println("开始接受消息！！！！");
            System.out.println(System.in.read());
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null && connection.isOpen()) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
