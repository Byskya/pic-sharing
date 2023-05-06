package com.example.picsharingspringboot.util;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class WebSocketManager {
//    保存指定用户的websocketSession
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
//    添加session
    public void addSession(String userId, WebSocketSession session) {
        sessions.put(userId, session);
    }
//删除session
    public void removeSession(String userId) {
        sessions.remove(userId);
    }
//发送信息给指定用户的后台逻辑
    public void sendMessageToUser(String userId, String message) throws IOException {
        WebSocketSession session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        }
    }
//通过广播的方式，给所有用户发送信息，由用户选择是否接收
    public void broadcastMessage(String message) throws IOException {
        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            }
        }
    }
}
