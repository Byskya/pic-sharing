package com.example.picsharingspringboot.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtil {
    /**
     * 将文件转换成byte[]数组
     * @param file 需要转换的文件
     * @return byte[]数组
     */
    public static byte[] fileToByteArray(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 8];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        in.close();
        return out.toByteArray();
    }
}

