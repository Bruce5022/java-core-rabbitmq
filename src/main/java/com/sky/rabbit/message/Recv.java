package com.sky.rabbit.message;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Recv {

	private final static String QUEUE_NAME = "queue-test-01";

	public static void main(String[] argv) throws Exception {
		// 1.创建一个连接工厂ConnectionFactory
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("49.234.60.118");
		factory.setUsername("sky-test-01");
		factory.setPassword("sky-test-01");
		factory.setVirtualHost("host-test-01");

		// 2.通过连接工厂建立连接
		Connection connection = factory.newConnection();

		// 3.通过连接创建通道
		Channel channel = connection.createChannel();

		// 4.声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println("Waiting for messages. To exit press CTRL+C");

		// 5.创建消费者开启监听
		channel.basicConsume(QUEUE_NAME, true, new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
									   byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
			}
		});
	}
}