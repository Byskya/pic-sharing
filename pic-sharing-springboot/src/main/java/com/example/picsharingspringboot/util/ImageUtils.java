package com.example.picsharingspringboot.util;

import java.io.*;

public class ImageUtils {
    // 从指定路径读取图片文件并返回字节数组
    public static byte[] readImage(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + path);
        }
        InputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        inputStream.close();
        return bytes;
    }

    // 将字节数组保存为图片文件到指定位置
    public static void saveImage(byte[] bytes, String path) throws IOException {
        OutputStream outputStream = new FileOutputStream(path);
        outputStream.write(bytes);
        outputStream.close();
    }
}

