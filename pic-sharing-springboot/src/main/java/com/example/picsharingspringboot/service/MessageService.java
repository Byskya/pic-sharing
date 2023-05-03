package com.example.picsharingspringboot.service;

import com.example.picsharingspringboot.entity.ChatMessage;
import com.example.picsharingspringboot.entity.ManagerMessage;
import com.example.picsharingspringboot.entity.User;

import java.util.List;

public interface MessageService {
    boolean saveMessage(ManagerMessage managerMessage);

    List<ManagerMessage> loadNotification();

    boolean deleteNotificationById(Integer id);

    boolean updateNotificationById(ManagerMessage managerMessage);

    boolean saveChatData(ChatMessage chatMessage);

    List<ChatMessage> getChatMessages(ChatMessage chatMessage);

    List<ChatMessage> getUserMessageList(User user);

    List<ChatMessage> getUserNotReadMessageNumber(User user);

    boolean setMessageIsRead(ChatMessage chatMessage);
}
