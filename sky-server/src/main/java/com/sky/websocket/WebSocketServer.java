package com.sky.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@ServerEndpoint("/ws/{sid}")
public class WebSocketServer {

    // 存放会话连接
    private static Map<String, Session> sessionMap = new HashMap<>();

    public WebSocketServer() {
        log.info("WebSocketServer init");
    }

    /**
     * 建立连接调用的方法
     * @param session
     * @param sid
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        sessionMap.put(sid, session);
        log.info("建立连接, 连接会话sid：{}", sid);
    }

    /**
     * 接收消息
     * @param message
     * @param sid
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        log.info("收到sid:{}的消息：{}", sid, message);
    }

    /**
     * 关闭连接
     * @param session
     * @param sid
     */
    @OnClose
    public void onClose(Session session, @PathParam("sid") String sid) {
        log.info("关闭连接, 连接会话sid：{}", sid);
        sessionMap.remove(sid);
    }

    /**
     * 给所有客户端发送消息
     * @param message
     */
    public void sendToAllClient(String message) {
        for (Session session : sessionMap.values()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
