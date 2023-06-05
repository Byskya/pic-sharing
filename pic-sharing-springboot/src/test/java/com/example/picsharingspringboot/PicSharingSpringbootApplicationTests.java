package com.example.picsharingspringboot;

import com.example.picsharingspringboot.service.MessageService;
import com.example.picsharingspringboot.service.UserService;
import com.example.picsharingspringboot.util.ImageUtils;
import com.example.picsharingspringboot.util.ThumbnailGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@SpringBootTest
@Component
class PicSharingSpringbootApplicationTests {
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Test
    void contextLoads() {
        System.out.println(userService);
        System.out.println(messageService);
    }
//    @Test
//    void test1() throws IOException {
//        byte[] bytes = ImageUtils.readImage("D:\\Tools\\MyDatabase\\illustrations\\1683211638310-1683210630588-1683210485853-1683210383770-1683210261431-1683209865967-夏の冒険.png");
//        String thumbnailBasePath = "D:\\Tools\\MyDatabase\\illustrations\\Thumbnails\\";
//        String thumbnailPath = thumbnailBasePath+System.currentTimeMillis()+"-thumbnail-"+"雲海.png";
//        byte[] bytesThumbnail = ThumbnailGenerator.generateThumbnail(bytes,"png");
//        ImageUtils.saveImage(bytesThumbnail,thumbnailPath);
//    }
}
