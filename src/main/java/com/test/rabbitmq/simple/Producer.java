package com.test.rabbitmq.simple;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

    public static void main(String[] args) {
        // 所有的中间件技术都是基于TCP/IP协议的 只不过rabbitmq遵循的是ampq协议
        // ip port(必要)
        // 1.创建连接工程
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("139.155.181.217");
        connectionFactory.setPort(5673);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        Channel channel = null;
        Connection connection = null;
        try {
            // 2.创建连接
            connection = connectionFactory.newConnection("生产者");
            // 3.创建连接获取通道
            channel = connection.createChannel();
            // 4.通过创建交换机，队列绑定关系，路由key，发送消息和接受消息
            String queueName = "queue4";
            /*
             * 队列的名称
             * 是否要持久化，所谓持久化消息是否存盘，如果false，非持久化true是持久化？非持久化会存盘吗？会存盘，但随着服务器重启会丢失数据
             * 排他性，是否独占独立
             * 是否自动删除？随着最后一个消费者信息完毕消息以后是否把队列自动删除
             * 携带附加属性
             */
            channel.queueDeclare(queueName, false, false, true, null);
            String message = "测试消息内容";
            // 5.准备消息内容
            // 6.发送消息给队列queue
            // 交换机 路由key 消息的状态控制 消息主题
            // 交换机是必须的，如果没有初始化交换机，那么系统会给一个默认的交换机
            String exchangeName = "fanout-exchange";
            String routeKye = "";
            String type = "funout";
            channel.basicPublish(exchangeName, routeKye, null, message.getBytes());
            System.out.println("消息发送成功！！！！");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            // 7.关闭连接
            // 8.关闭通道
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
