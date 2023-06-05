package com.example.picsharingspringboot.controller;

import com.example.picsharingspringboot.entity.ChatMessage;
import com.example.picsharingspringboot.entity.ManagerMessage;
import com.example.picsharingspringboot.entity.User;
import com.example.picsharingspringboot.service.MessageService;
import com.example.picsharingspringboot.service.UserService;
import com.example.picsharingspringboot.util.ResponseResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

//设置接收前端发送请求中携带的cookie
@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081","http://192.168.173.223:8081","http://192.168.173.223:8080"}, allowCredentials = "true")
@RestController
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    //获取所有的公告通知
    @GetMapping("/notifications/{pageNum}/{pageSize}")
    public ResponseResult<PageInfo<ManagerMessage>> getAllNotifications(@PathVariable("pageNum") Integer pageNun,@PathVariable("pageSize") Integer pageSize){
        PageHelper.startPage(pageNun,pageSize);
        ResponseResult<PageInfo<ManagerMessage>> rr = new ResponseResult<>();
        List<ManagerMessage> list = messageService.loadNotification();
        PageInfo<ManagerMessage> pageInfo = new PageInfo<>(list);
        if (!list.isEmpty()){
            rr.setMessage("获取所有通知成功");
            rr.setState(200);
            rr.setData(pageInfo);
        }
        else {
            rr.setMessage("获取所有通知失败");
            rr.setState(500);
            rr.setData(null);
        }
        return rr;
    }
//    删除通知
    @DeleteMapping("/notification/delete/{id}")
    public ResponseResult<Void> deleteNotificationById(@PathVariable("id")Integer id){
        ResponseResult<Void> rr = new ResponseResult<>();
        boolean judge = messageService.deleteNotificationById(id);
        if (judge){
            rr.setMessage("删除通知成功");
            rr.setState(200);
        }
        else {
            rr.setMessage("删除通知失败");
            rr.setState(500);
        }
        return rr;
    }
//    修改通知
    @PutMapping("/notification/update/{id}/{content}")
    public ResponseResult<Void> updateNotificationById(@PathVariable("id") Integer id,@PathVariable("content") String content){
        ResponseResult<Void> rr = new ResponseResult<>();
        ManagerMessage managerMessage = new ManagerMessage();
        managerMessage.setId(id);
        managerMessage.setContent(content);
        boolean judge = messageService.updateNotificationById(managerMessage);
        if (judge){
            rr.setMessage("修改通知成功");
            rr.setState(200);
        }
        else {
            rr.setMessage("修改通知失败");
            rr.setState(500);
        }
        return rr;
    }
//    获取用户聊天记录和双方的个人信息
    @GetMapping("/user/chat/{senderId}/{receiverId}")
    @Transactional
    public ResponseResult<Map<String,Object>> getChatData(@PathVariable("senderId")Integer senderId, @PathVariable("receiverId")Integer receiverId, HttpSession session){
        User sender = new User();
        User receiver = new User();
        sender.setId(senderId);
        receiver.setId(receiverId);
        sender = userService.getUserInfo(sender);
        receiver = userService.getUserInfo(receiver);
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSenderId(senderId);
        chatMessage.setReceiverId(receiverId);
        System.out.println(chatMessage);
        List<ChatMessage> list = messageService.getChatMessages(chatMessage);
        System.out.println(list);
        ResponseResult<Map<String,Object>> rr = new ResponseResult<>();
        Map<String,Object> chatData = new HashMap<>();
        if (sender!=null && receiver!=null){
            if (!list.isEmpty()){
                messageService.setMessageIsRead(chatMessage);
            }
            chatData.put("sender",sender);
            chatData.put("receiver",receiver);
            chatData.put("chatMessages",list);
            rr.setState(200);
            rr.setMessage("加载用户聊天数据成功");
            rr.setData(chatData);
        }
        else {
            rr.setState(500);
            rr.setMessage("加载用户聊天数据失败");
            rr.setData(null);
        }
        return rr;
    }
    //接收发送过来的信息保存到数据库中
    @PostMapping("/send/message/{senderId}/{receiverId}/{message}")
    public ResponseResult<Void> saveChatData(@PathVariable("senderId") Integer senderId,@PathVariable("receiverId")Integer receiverId,@PathVariable("message")String message,HttpSession session){
        ResponseResult<Void> rr = new ResponseResult<>();
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSenderId(senderId);
        chatMessage.setReceiverId(receiverId);
        chatMessage.setMessage(message);
        boolean judge = messageService.saveChatData(chatMessage);
        if (judge){
            rr.setMessage("保存信息成功");
            rr.setState(200);
        }
        else {
            rr.setMessage("保存信息失败");
            rr.setState(500);
        }
        return rr;
    }
    //获取当前用户的消息列表
    @GetMapping("/receive/allUserMessages")
    public ResponseResult<Map<String,Object>> getUserMessageList(HttpSession session){
        ResponseResult<Map<String,Object>> rr = new ResponseResult<>();
        User user = (User) session.getAttribute("user");
        if (user == null){
            rr.setMessage("请先登录");
            rr.setState(400);
            return rr;
        }
        List<ChatMessage> list = messageService.getUserMessageList(user);
        System.out.println(list);
        HashSet<User> userList = new HashSet<>();
        User user2 = new User();
        for (ChatMessage chatMessage : list) {
            if (chatMessage.getReceiverId().equals(user.getId())){
                user2.setId(chatMessage.getSenderId());
            }
            else {
                user2.setId(chatMessage.getReceiverId());
            }
            userList.add(userService.getUserInfo(user2));
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("userList",userList);
        map.put("messageList",list);
        rr.setData(map);
        rr.setMessage("获取消息列表成功");
        rr.setState(200);
        return rr;
    }
//    获取消息数量
    @GetMapping("/message/number")
    public ResponseResult<Integer> getUserNotReadMessageNumber(HttpSession session){
        ResponseResult<Integer> rr = new ResponseResult<>();
        User user = (User) session.getAttribute("user");
        if (user == null){
            rr.setMessage("请先登录");
            rr.setState(400);
            return rr;
        }
        List<ChatMessage> list = messageService.getUserNotReadMessageNumber(user);
        Integer messageNumber = list.size();
        System.out.println(list);
        rr.setData(messageNumber);
        rr.setMessage("获取消息数量成功");
        rr.setState(200);
        return rr;
    }
}
