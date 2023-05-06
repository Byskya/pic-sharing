package com.example.picsharingspringboot.controller;

import com.example.picsharingspringboot.entity.ManagerMessage;
import com.example.picsharingspringboot.service.MessageService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import javax.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint(value = "/managerMessage")
public class ManagerWebSocketHandler implements org.springframework.web.socket.WebSocketHandler {
    private final MessageService messageService;

    public ManagerWebSocketHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("afterConnectionEstablished");

    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println(message);
        // 将接收到的消息保存到数据库
        ManagerMessage managerMessage = new ManagerMessage();
        managerMessage.setContent((String) message.getPayload());
        boolean result = messageService.saveMessage(managerMessage);
        if (result) {
            System.out.println("保存成功");
        } else {
            System.out.println("保存失败");
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("afterConnectionClosed");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
