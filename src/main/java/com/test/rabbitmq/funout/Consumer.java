package com.test.rabbitmq.funout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static void main(String[] args) {
        new Thread(new MyThread(), "queue1").start();
        new Thread(new MyThread(), "queue2").start();
        new Thread(new MyThread(), "queue3").start();
    }

    public static class MyThread implements Runnable {
        @Override
        public void run() {
            String queueName = Thread.currentThread().getName();
            // 所有的中间件技术都是基于TCP/IP协议的 只不过rabbitmq遵循的是amqp协议
            // ip port(必要)
            // 1.创建连接工程
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("139.155.181.217");
            connectionFactory.setPort(5673);
            connectionFactory.setUsername("admin");
            connectionFactory.setPassword("admin");
            connectionFactory.setVirtualHost("/");
            Connection connection = null;
            Channel channel = null;
            try {
                connection = connectionFactory.newConnection("测试连接");
                channel = connection.createChannel();
                channel.basicConsume(queueName, true, (s, deliver) -> {
                    System.out.println(queueName + "接收到的消息是" + new String(deliver.getBody(), StandardCharsets.UTF_8));
                }, (s) -> {
                    System.out.println(queueName + "获取消息失败" + s);
                });
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

}
