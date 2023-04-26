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
}
