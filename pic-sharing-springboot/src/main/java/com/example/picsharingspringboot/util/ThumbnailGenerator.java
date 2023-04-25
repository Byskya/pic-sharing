package com.example.picsharingspringboot.util;
import net.coobird.thumbnailator.Thumbnails;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ThumbnailGenerator {

    private static final int THUMBNAIL_SIZE = 400;

    /**
     * 生成缩略图并返回字节数组
     *
     * @param originalImage 原始图像的字节数组
     * @param format        图像格式（如：png、jpg、jpeg等）
     * @return 缩略图的字节数组
     */
    public static byte[] generateThumbnail(byte[] originalImage, String format) throws IOException {
        BufferedImage originalBufferedImage = ImageIO.read(new ByteArrayInputStream(originalImage));
        BufferedImage thumbnailBufferedImage = Thumbnails.of(originalBufferedImage)
                .size(THUMBNAIL_SIZE, THUMBNAIL_SIZE)
                .asBufferedImage();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(thumbnailBufferedImage).scale(1.5)
                .outputFormat(format)
                .toOutputStream(outputStream);
        return outputStream.toByteArray();
    }
}


