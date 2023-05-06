package com.example.picsharingspringboot.controller;

import com.example.picsharingspringboot.service.MessageService;
import com.example.picsharingspringboot.util.WebSocketManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;

@ServerEndpoint(value = "/UserChat/{userId}")
@Component
public class UserChatWebSocketHandler implements WebSocketHandler {
//    信息业务逻辑处理类
    private final MessageService messageService;
//    websocket端口统一管理对象
    private final WebSocketManager webSocketManager;
//    JSON转换成其他类型工具
    private final ObjectMapper objectMapper = new ObjectMapper();
    public UserChatWebSocketHandler(MessageService messageService,WebSocketManager webSocketManager) {
        this.messageService = messageService;
        this.webSocketManager = webSocketManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, Object> map = session.getAttributes();
        webSocketManager.addSession(map.get("userId").toString(),session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message ) throws Exception {
        System.out.println(message.getPayload());
        String payload = message.getPayload().toString();
        Map<String, Object> map = objectMapper.readValue(payload,new TypeReference<Map<String, Object>>() {});
        webSocketManager.sendMessageToUser(map.get("receiver").toString(),map.get("message").toString());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        webSocketManager.removeSession(attributes.get("userId").toString());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
