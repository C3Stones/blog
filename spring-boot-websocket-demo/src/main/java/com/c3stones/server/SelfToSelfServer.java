package com.c3stones.server;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * <b style="color: blue"> 自己给自己发送消息 </b>
 * 
 * @author CL
 *
 */
@Slf4j
@Component
@ServerEndpoint(value = "/selfToSelf")
public class SelfToSelfServer {

	/**
	 * 在线数
	 * <p>
	 * 多线程环境下，为了保证线程安全
	 * </p>
	 */
	private static AtomicInteger online = new AtomicInteger(0);

	/**
	 * 建立连接
	 * 
	 * @param session 客户端连接对象
	 */
	@OnOpen
	public void onOpen(Session session) {
		// 在线数加1
		online.incrementAndGet();
		log.info("客户端连接建立成功，Session ID：{}，当前在线数：{}", session.getId(), online.get());
	}

	/**
	 * 接收客户端消息
	 * 
	 * @param message 客户端发送的消息内容
	 * @param session 客户端连接对象
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		log.info("服务端接收消息成功，Session ID：{}，消息内容：{}", session.getId(), message);

		// 处理消息，并响应给客户端
		this.sendMessage(message, session);
	}

	/**
	 * 处理消息，并响应给客户端
	 * 
	 * @param message 客户端发送的消息内容
	 * @param session 客户端连接对象
	 */
	private void sendMessage(String message, Session session) {
		try {
			String response = "Server Response ==> " + message;
			session.getBasicRemote().sendText(response);

			log.info("服务端响应消息成功，接收的Session ID：{}，响应内容：{}", session.getId(), response);
		} catch (IOException e) {
			log.error("服务端响应消息异常：{}", e.getMessage());
		}
	}

	/**
	 * 关闭连接
	 * 
	 * @param session 客户端连接对象
	 */
	@OnClose
	public void onClose(Session session) {
		// 在线数减1
		online.decrementAndGet();
		log.info("客户端连接关闭成功，Session ID：{}，当前在线数：{}", session.getId(), online.get());
	}

	/**
	 * 连接异常
	 * 
	 * @param session 客户端连接对象
	 * @param error   异常
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		log.error("连接异常：{}", error);
	}

}
