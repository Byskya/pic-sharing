package com.example.picsharingspringboot.util;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class MD5Util {
    // 获取字符串的MD5值
    public static String getMD5(String message) {
        String md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageByte = message.getBytes(StandardCharsets.UTF_8);
            byte[] md5Byte = md.digest(messageByte);
            md5 = bytesToHex(md5Byte);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }

    // 将字节数组转换为十六进制字符串
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        String strTemp;
        for (byte aByte : bytes) {
            strTemp = Integer.toHexString(aByte & 0xFF);
            if (strTemp.length() == 1) {
                hexString.append("0");
            }
            hexString.append(strTemp);
        }
        return hexString.toString();
    }

    // 随机生成盐值
    public static String generateSalt() {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int number = (int) (Math.random() * base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    // 获取加盐后的MD5值
    public static String getSaltMD5(String password, String salt) {
        password = getMD5(password + salt);
        return password;
    }
}
