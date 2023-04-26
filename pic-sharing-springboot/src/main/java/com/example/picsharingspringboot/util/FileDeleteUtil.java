package com.example.picsharingspringboot.util;

import java.io.File;
import java.util.Objects;

public class FileDeleteUtil {
    /**
     * 删除指定位置的文件或目录
     *
     * @param file 文件或目录
     * @return 删除是否成功
     */
    public static boolean deleteFile(File file) {
        if (file.isDirectory()) {
            // 如果是目录，则递归删除目录下的文件
            for (File subFile : Objects.requireNonNull(file.listFiles())) {
                if (!deleteFile(subFile)) {
                    return false;  // 如果删除子文件不成功，则删除失败
                }
            }
        }
        return file.delete();  // 删除当前文件
    }
}
