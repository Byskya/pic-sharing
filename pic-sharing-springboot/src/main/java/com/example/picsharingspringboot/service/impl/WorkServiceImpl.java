package com.example.picsharingspringboot.service.impl;

import com.example.picsharingspringboot.entity.*;
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

    @Override
    public List<IllustrationTag> getWorkTagsById(Integer workId) {
        List<IllustrationTag> tags = workMapper.getWorkTagsById(workId);
        return tags;
    }

    @Override
    public List<Illustration> getAllIllustration() {
        List<Illustration> list = workMapper.getAllIllustration();
        return list;
    }

    @Override
    public boolean addCommentByWorkId(Comment comment) {
        boolean judge = workMapper.addCommentByWorkId(comment);
        return judge;
    }

    @Override
    public List<Comment> getIllustrationCommentById(Integer workId) {
        return workMapper.getIllustrationCommentById(workId);
    }

    @Override
    public Illustration getIllustrationById(Illustration illustration) {
        return workMapper.getIllustrationById(illustration);
    }

    @Override
    public boolean upWorkViews(Illustration illustration) {
        return workMapper.upWorkViews(illustration);
    }

    @Override
    public boolean upWorkLikes(Illustration illustration) {
        return workMapper.upWorkLike(illustration);
    }

    @Override
    public boolean collectWork(Favorite favorite) {
        return workMapper.collectWork(favorite);
    }

    @Override
    public Favorite checkCollect(Favorite favorite) {
        return workMapper.checkCollect(favorite);
    }

    @Override
    public boolean deleteCollect(Favorite favorite) {
        return workMapper.deleteCollect(favorite);
    }

    @Override
    public List<Illustration> getNotApprovedWorks() {
        return workMapper.getNotApprovedWorks();
    }

    @Override
    public boolean reviewPass(Illustration illustration) {
        return workMapper.reviewPass(illustration);
    }

    @Override
    public boolean updateAuditTable(AuditInfo auditInfo) {
        return workMapper.updateAuditTable(auditInfo);
    }

    @Override
    public boolean addAuditInfo(AuditInfo auditInfo) {
        return workMapper.addAuditInfo(auditInfo);
    }

    @Override
    public AuditInfo getAuditInfoByWorkId(Integer workId) {
        return workMapper.getAuditInfoByWorkId(workId);
    }
}
