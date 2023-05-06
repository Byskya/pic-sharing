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

    @Override
    public List<Illustration> getUserCollection(User user) {
        return workMapper.getUserCollection(user);
    }

    @Override
    public List<Illustration> searchWorkByKeyword(String keyword) {
        return workMapper.getIllustrationsByKeyword(keyword);
    }

    @Override
    public Illustration getReturnWorkById(Integer workId) {
        return workMapper.getReturnWorkById(workId);
    }

    @Override
    public boolean editImage(Image image) {
        return workMapper.editImage(image);
    }

    @Override
    public boolean editIllustration(Illustration info) {
        return workMapper.editIllustration(info);
    }

    @Override
    public boolean deleteWorkTags(Integer id) {
        return workMapper.deleteWorkTags(id);
    }

    @Override
    public List<Illustration> getUserAllIllustrationExceptUnapproved(Integer userId) {
        return workMapper.getUserAllIllustrationExceptUnapproved(userId);
    }

    @Override
    public List<Illustration> getFollowingWorksByUserId(Follow follow) {
        return workMapper.getFollowingWorksByUserId(follow);
    }

    @Override
    public List<Illustration> getAllIllustrationIncludeUnapproved() {
        return workMapper.getAllIllustrationIncludeUnapproved();
    }

    @Override
    public boolean takeoffWork(Illustration illustration) {
        return workMapper.takeoffWork(illustration);
    }

    @Override
    public List<Illustration> getIllustrationByTag(IllustrationTag illustrationTag) {
        return workMapper.getIllustrationByTag(illustrationTag);
    }

    @Override
    public List<Illustration> getRankWorks() {
        List<Illustration> allIllustration = workMapper.getAllIllustration();
        allIllustration.sort((i1,i2)->(i2.getViews()-i1.getViews()));
        List<Illustration> top10 = allIllustration.subList(0, Math.min(10, allIllustration.size()));
        return top10;
    }

    @Override
    public boolean addWorkTag(IllustrationTag illustrationTag) {
        return workMapper.addWorkTag(illustrationTag);
    }

    @Override
    public boolean editWorkTag(IllustrationTag illustrationTag) {
        return workMapper.editWorkTag(illustrationTag);
    }

    @Override
    public boolean deleteWorkTagById(IllustrationTag illustrationTag) {
        return workMapper.deleteWorkTagById(illustrationTag);
    }

    @Override
    public Illustration getWorkInfoById(Integer workId) {
        return workMapper.getEditWorkInfoById(workId);
    }

    @Override
    public boolean deleteWorkById(Illustration illustration) {
        return workMapper.deleteWorkById(illustration);
    }

    @Override
    public boolean recordWatchHistory(UserHistory userHistory) {
        return workMapper.recordUserHistory(userHistory);
    }

    @Override
    public UserHistory findUserHistory(UserHistory userHistory) {
        return workMapper.findUserHistory(userHistory);
    }

    @Override
    public boolean updateUserHistory(UserHistory userHistory) {
        return workMapper.updateUserHistory(userHistory);
    }

    @Override
    public List<UserHistory> getUserWatchHistory(Integer userId) {
        return workMapper.getUserWatchHistory(userId);
    }

    @Override
    public Illustration getIllustrationThumbnailById(Illustration illustration) {
        return workMapper.getIllustrationThumbnailById(illustration);
    }

    @Override
    public boolean deleteUserWatchWorkHistory(UserHistory history) {
        return workMapper.deleteUserHistoryById(history);
    }

    @Override
    public boolean deleteUserAllHistory(Integer userId) {
        return workMapper.deleteUserAllHistoryByUserId(userId);
    }

    @Override
    public boolean deleteImageTableDataById(Integer workId) {
        return workMapper.deleteImageTableDataById(workId);
    }

    @Override
    public boolean likeWork(Like like) {
        return workMapper.likeWork(like);
    }

    @Override
    public Favorite checkLikes(Like like) {
        return workMapper.checkLikes(like);
    }

    @Override
    public boolean deleteLike(Like like) {
        return workMapper.deleteLike(like);
    }
}
