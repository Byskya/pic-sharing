package com.example.picsharingspringboot.mapper;

import com.example.picsharingspringboot.entity.ChatMessage;
import com.example.picsharingspringboot.entity.ManagerMessage;
import com.example.picsharingspringboot.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    boolean saveMessage(ManagerMessage managerMessage);

    List<ManagerMessage> getAllNotifications();

    boolean deleteNotificationById(Integer id);

    boolean updateNotification(ManagerMessage managerMessage);

    boolean saveChatData(ChatMessage chatMessage);

    List<ChatMessage> getChatMessages(ChatMessage chatMessage);

    List<ChatMessage> getUserMessageList(User user);

    List<ChatMessage> getUserNotReadMessageNumber(User user);

    boolean setMessageIsRead(ChatMessage chatMessage);
}
