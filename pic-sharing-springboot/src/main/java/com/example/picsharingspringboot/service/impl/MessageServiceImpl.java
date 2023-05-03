package com.example.picsharingspringboot.service.impl;

import com.example.picsharingspringboot.entity.ChatMessage;
import com.example.picsharingspringboot.entity.ManagerMessage;
import com.example.picsharingspringboot.entity.User;
import com.example.picsharingspringboot.mapper.MessageMapper;
import com.example.picsharingspringboot.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageMapper messageMapper;

    public MessageServiceImpl(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Override
    public boolean saveMessage(ManagerMessage managerMessage) {
        return messageMapper.saveMessage(managerMessage);
    }

    @Override
    public List<ManagerMessage> loadNotification() {
        return messageMapper.getAllNotifications();
    }

    @Override
    public boolean deleteNotificationById(Integer id) {
        return messageMapper.deleteNotificationById(id);
    }

    @Override
    public boolean updateNotificationById(ManagerMessage managerMessage) {
        return messageMapper.updateNotification(managerMessage);
    }

    @Override
    public boolean saveChatData(ChatMessage chatMessage) {
        return messageMapper.saveChatData(chatMessage);
    }

    @Override
    public List<ChatMessage> getChatMessages(ChatMessage chatMessage) {
        return messageMapper.getChatMessages(chatMessage);
    }

    @Override
    public List<ChatMessage> getUserMessageList(User user) {
        return messageMapper.getUserMessageList(user);
    }

    @Override
    public List<ChatMessage> getUserNotReadMessageNumber(User user) {
        return messageMapper.getUserNotReadMessageNumber(user);
    }

    @Override
    public boolean setMessageIsRead(ChatMessage chatMessage) {
        return messageMapper.setMessageIsRead(chatMessage);
    }
}
