package com.example.picsharingspringboot.mapper;

import com.example.picsharingspringboot.entity.Illustration;
import com.example.picsharingspringboot.entity.IllustrationTag;
import com.example.picsharingspringboot.entity.Image;
import com.example.picsharingspringboot.entity.MapIllustrationTag;
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
}
