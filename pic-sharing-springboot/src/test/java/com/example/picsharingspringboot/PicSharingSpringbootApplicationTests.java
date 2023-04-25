package com.example.picsharingspringboot;

import com.example.picsharingspringboot.util.ImageUtils;
import com.example.picsharingspringboot.util.ThumbnailGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Objects;

@SpringBootTest
class PicSharingSpringbootApplicationTests {

    @Test
    void contextLoads() {
    }
//    @Test
//    void test1() throws IOException {
//        byte[] bytes = ImageUtils.readImage("D:\\Tools\\MyDatabase\\illustrations\\1681904609134-header.jpg");
//        String thumbnailBasePath = "D:\\Tools\\MyDatabase\\illustrations\\Thumbnails\\";
//        String thumbnailPath = thumbnailBasePath+System.currentTimeMillis()+"-thumbnail-"+"header.jpg";
//        byte[] bytesThumbnail = ThumbnailGenerator.generateThumbnail(bytes,"jpg");
//        ImageUtils.saveImage(bytesThumbnail,thumbnailPath);
//    }
}
