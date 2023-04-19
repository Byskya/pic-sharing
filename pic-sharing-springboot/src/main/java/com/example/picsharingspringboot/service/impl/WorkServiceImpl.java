package com.example.picsharingspringboot.service.impl;

import com.example.picsharingspringboot.entity.Illustration;
import com.example.picsharingspringboot.entity.IllustrationTag;
import com.example.picsharingspringboot.entity.Image;
import com.example.picsharingspringboot.entity.MapIllustrationTag;
import com.example.picsharingspringboot.mapper.WorkMapper;
import com.example.picsharingspringboot.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkServiceImpl implements WorkService {
    @Autowired
    WorkMapper workMapper;
    @Override
    public boolean uploadImage(Image image) {
        boolean judge = workMapper.insertImage(image);
        return judge;
    }

    @Override
    public boolean uploadIllustration(Illustration info) {
        boolean judge = workMapper.insertIllustration(info);
        if (judge){
            return true;
        }
        else {
            return false;
        }

    }

    @Override
    public List<IllustrationTag> getAllTags() {
        List<IllustrationTag> tags = workMapper.getAllTags();
        return tags;
    }

    @Override
    public boolean addWorkTagMap(List<MapIllustrationTag> listMap) {
        boolean judge = workMapper.insertWorkTagMap(listMap);
        if (judge){
            return true;
        }
        else {
            return false;
        }
    }
//    获取指定用户的所有作品
    @Override
    public List<Illustration> getUserAllIllustration(Integer userId) {
        List<Illustration> list = workMapper.getAllUserIllustrations(userId);
        if (list!=null){
            return list;
        }
        else {
            return null;
        }
    }

    @Override
    public Image getImageById(Integer imageId) {
        Image image = workMapper.getImageById(imageId);
        if (image!=null){
            return image;
        }
        else {
            return null;
        }
    }
}
