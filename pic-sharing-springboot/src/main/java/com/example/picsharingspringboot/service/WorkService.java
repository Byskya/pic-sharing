package com.example.picsharingspringboot.service;

import com.example.picsharingspringboot.entity.Illustration;
import com.example.picsharingspringboot.entity.IllustrationTag;
import com.example.picsharingspringboot.entity.Image;
import com.example.picsharingspringboot.entity.MapIllustrationTag;

import java.util.List;

public interface WorkService {
    boolean uploadImage(Image image);

    boolean uploadIllustration(Illustration info);

    List<IllustrationTag> getAllTags();

    boolean addWorkTagMap(List<MapIllustrationTag> listMap);

    List<Illustration> getUserAllIllustration(Integer userId);

    Image getImageById(Integer imageId);
}
