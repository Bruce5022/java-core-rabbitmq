package com.sky.rabbit.exchange.fanout;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Customer {
	public static void main(String[] argv) throws Exception {
		// 1.创建一个连接工厂ConnectionFactory
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("49.234.60.118");
		factory.setUsername("sky-test-01");
		factory.setPassword("sky-test-01");
		factory.setVirtualHost("host-test-01");
		factory.setAutomaticRecoveryEnabled(true);
		factory.setNetworkRecoveryInterval(3000);

		// 2.通过连接工厂建立连接
		Connection connection = factory.newConnection();

		// 3.通过连接创建通道
		Channel channel = connection.createChannel();

		// 4.声明
		String exchangeName = "test_fanout_exchange";
		String exchangeType = "fanout";
		String queueName = "test_fanout_queue";
		String routingKey = "";// 不设置路由键
		channel.exchangeDeclare(exchangeName,exchangeType,true,false,false,null);
		channel.queueDeclare(queueName, false, false, false, null);
		channel.queueBind(queueName,exchangeName,routingKey);
		System.out.println("等待消息... To exit press CTRL+C");

		// 5.创建消费者开启监听
		channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
									   byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println("消费者获取到消息： " + message);
			}
		});
	}
}