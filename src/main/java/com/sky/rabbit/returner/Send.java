package com.sky.rabbit.returner;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Send {

	private final static String QUEUE_NAME = "queue-test-011";

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

		// 4.发布消息
		String message = "Hello World!";


		// 这里需要注意:第一个参数是exchange,如果这个为"",会走默认的Default路由器,这个默认路由会绑定所有的队列,这个路由器会根据routingKey把消息投递到
		// 队列名为routingKey值的地方,如果有这样的队列,就会路由过去,如果没有这样的队列,就彻底发送失败了,这个消息就会被删除掉
		channel.basicPublish("", QUEUE_NAME,true, null, message.getBytes("UTF-8"));
		System.out.println(" [x] Sent '" + message + "'");

		channel.addConfirmListener(new ConfirmListener() {
			@Override
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("Ack成功："+deliveryTag);
			}

			@Override
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("Ack失败："+deliveryTag);
			}
		});

		channel.addReturnListener(new ReturnListener() {
			@Override
			public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + replyText + "'");
			}
		});

//		channel.close();
//		connection.close();
	}
}