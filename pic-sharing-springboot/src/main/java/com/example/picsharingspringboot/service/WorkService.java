package com.example.picsharingspringboot.service;

import com.example.picsharingspringboot.entity.*;

import java.util.List;

public interface WorkService {
    boolean uploadImage(Image image);

    boolean uploadIllustration(Illustration info);

    List<IllustrationTag> getAllTags();

    boolean addWorkTagMap(List<MapIllustrationTag> listMap);

    List<Illustration> getUserAllIllustration(Integer userId);

    Image getImageById(Integer imageId);

    List<IllustrationTag> getWorkTagsById(Integer workId);

    List<Illustration> getAllIllustration();

    boolean addCommentByWorkId(Comment comment);

    List<Comment> getIllustrationCommentById(Integer workId);

    Illustration getIllustrationById(Illustration illustration);

    boolean upWorkViews(Illustration illustration);

    boolean upWorkLikes(Illustration illustration);

    boolean collectWork(Favorite favorite);

    Favorite checkCollect(Favorite favorite);

    boolean deleteCollect(Favorite favorite);

    List<Illustration> getNotApprovedWorks();

    boolean reviewPass(Illustration illustration);

    boolean updateAuditTable(AuditInfo auditInfo);

    boolean addAuditInfo(AuditInfo auditInfo);

    AuditInfo getAuditInfoByWorkId(Integer workId);

    List<Illustration> getUserCollection(User user);

    List<Illustration> searchWorkByKeyword(String keyword);

    Illustration getReturnWorkById(Integer workId);

    boolean editImage(Image image);

    boolean editIllustration(Illustration info);

    boolean deleteWorkTags(Integer id);

    List<Illustration> getUserAllIllustrationExceptUnapproved(Integer userId);

    List<Illustration> getFollowingWorksByUserId(Follow follow);

    List<Illustration> getAllIllustrationIncludeUnapproved();

    boolean takeoffWork(Illustration illustration);

    List<Illustration> getIllustrationByTag(IllustrationTag illustrationTag);

    List<Illustration> getRankWorks();

    boolean addWorkTag(IllustrationTag illustrationTag);

    boolean editWorkTag(IllustrationTag illustrationTag);

    boolean deleteWorkTagById(IllustrationTag illustrationTag);

    Illustration getWorkInfoById(Integer workId);

    boolean deleteWorkById(Illustration illustration);

    boolean recordWatchHistory(UserHistory userHistory);

    UserHistory findUserHistory(UserHistory userHistory);

    boolean updateUserHistory(UserHistory userHistory);

    List<UserHistory> getUserWatchHistory(Integer id);

    Illustration getIllustrationThumbnailById(Illustration illustration);

    boolean deleteUserWatchWorkHistory(UserHistory history);

    boolean deleteUserAllHistory(Integer userId);

    boolean deleteImageTableDataById(Integer workId);
}
