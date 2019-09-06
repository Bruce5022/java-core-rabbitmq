package com.sky.rabbit.exchange.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {


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
		channel.basicPublish("test_direct_exchange", "test.direct", null, message.getBytes("UTF-8"));
		System.out.println(" [x] Sent '" + message + "'");

		channel.close();
		connection.close();
	}
}