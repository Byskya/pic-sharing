package com.example.picsharingspringboot.controller;

import com.example.picsharingspringboot.entity.ChatMessage;
import com.example.picsharingspringboot.entity.Illustration;
import com.example.picsharingspringboot.entity.IllustrationTag;
import com.example.picsharingspringboot.entity.User;
import com.example.picsharingspringboot.service.MessageService;
import com.example.picsharingspringboot.service.UserService;
import com.example.picsharingspringboot.service.WorkService;
import com.example.picsharingspringboot.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8081","http://192.168.173.223:8081","http://192.168.173.223:8080"}, allowCredentials = "true")
@RestController
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    WorkService workService;
    @Autowired
    MessageService messageService;
    @GetMapping("/platform/data")
    public ResponseResult<Map<String,Object>> getPlatformData(){
        ResponseResult<Map<String,Object>> rr = new ResponseResult<>();
        List<User> allUser = userService.getAllUser();
        List<Illustration> allIllustrationIncludeUnapproved = workService.getAllIllustrationIncludeUnapproved();
        List<ChatMessage> allChatMessage = messageService.getAllMessage();
        List<IllustrationTag> allTags = workService.getAllTags();
//        获取男女比例信息
        int female = 0;
        int male = 0;
        int fm = 0;
        for (User user : allUser) {
            if (user.getGender() == '男' || user.getGender() == 'M') {
                male++;
            } else if (user.getGender() == '女' || user.getGender() == 'F') {
                female++;
            }
            else {
                fm++;
            }
        }
        HashMap<String, Object> map = new HashMap<>();
        IllustrationTag illustrationTag = new IllustrationTag();
        HashMap<String, Object> tagMap = new HashMap<>();
        for (IllustrationTag allTag : allTags) {
            illustrationTag.setId(allTag.getId());
            illustrationTag.setTagName(allTag.getTagName());
            List<Illustration> illustrationByTag = workService.getIllustrationByTag(illustrationTag);
            tagMap.put(allTag.getTagName(),illustrationByTag.size());
        }
        map.put("tagMap",tagMap);
        map.put("tagList",allTags);
        map.put("female",female);
        map.put("male",male);
        map.put("fm",fm);
        map.put("userList",allUser);
        map.put("workList",allIllustrationIncludeUnapproved);
        map.put("userNumber",allUser.size());
        map.put("workNumber",allIllustrationIncludeUnapproved.size());
        map.put("messageNumber",allChatMessage.size());
        rr.setData(map);
        return rr;
    }
}
