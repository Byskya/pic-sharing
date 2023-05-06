package com.example.picsharingspringboot.mapper;

import com.example.picsharingspringboot.entity.*;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;


@Mapper
public interface WorkMapper {
    boolean insertImage(Image image);

    boolean insertIllustration(Illustration info);

    List<IllustrationTag> getAllTags();

    boolean insertWorkTagMap(List<MapIllustrationTag> list);

    List<Illustration> getAllUserIllustrations(Integer userId);

    Image getImageById(Integer imageId);

    List<IllustrationTag> getWorkTagsById(Integer workId);

    List<Illustration> getAllIllustration();

    boolean addCommentByWorkId(Comment comment);

    List<Comment> getIllustrationCommentById(Integer workId);

    Illustration getIllustrationById(Illustration illustration);

    boolean upWorkViews(Illustration illustration);

    boolean upWorkLike(Illustration illustration);

    boolean collectWork(Favorite favorite);

    Favorite checkCollect(Favorite favorite);

    boolean deleteCollect(Favorite favorite);

    List<Illustration> getNotApprovedWorks();

    boolean reviewPass(Illustration illustration);

    boolean updateAuditTable(AuditInfo auditInfo);

    boolean addAuditInfo(AuditInfo auditInfo);

    AuditInfo getAuditInfoByWorkId(Integer workId);

    List<Illustration> getUserCollection(User user);

    List<Illustration> getIllustrationsByKeyword(String keyword);

    Illustration getReturnWorkById(Integer workId);

    boolean editImage(Image image);

    boolean editIllustration(Illustration info);

    boolean deleteWorkTags(Integer id);

    List<Illustration> getUserAllIllustrationExceptUnapproved(Integer userId);

    List<Illustration> getFollowingWorksByUserId(Follow follow);

    List<Illustration> getAllIllustrationIncludeUnapproved();

    boolean takeoffWork(Illustration illustration);

    List<Illustration> getIllustrationByTag(IllustrationTag illustrationTag);

    boolean addWorkTag(IllustrationTag illustrationTag);

    boolean editWorkTag(IllustrationTag illustrationTag);

    boolean deleteWorkTagById(IllustrationTag illustrationTag);

    Illustration getEditWorkInfoById(Integer workId);

    boolean deleteWorkById(Illustration illustration);

    boolean recordUserHistory(UserHistory userHistory);

    UserHistory findUserHistory(UserHistory userHistory);

    boolean updateUserHistory(UserHistory userHistory);

    List<UserHistory> getUserWatchHistory(Integer userId);

    Illustration getIllustrationThumbnailById(Illustration illustration);

    boolean deleteUserHistoryById(UserHistory history);

    boolean deleteUserAllHistoryByUserId(Integer userId);

    boolean deleteImageTableDataById(Integer workId);

    boolean likeWork(Like like);

    Favorite checkLikes(Like like);

    boolean deleteLike(Like like);
}
