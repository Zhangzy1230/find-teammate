package com.zzy.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;  
  
public class PasswordEncrypterWithoutSalt {  
  
    // 使用SHA-256算法生成密码的哈希值（不使用盐值）  
    public static String generateHashedPassword(String password) {  
        try {  
            // 创建MessageDigest实例，初始化为SHA-256算法  
            MessageDigest digest = MessageDigest.getInstance("SHA-256");  
  
            // 计算密码的字节数组  
            byte[] passwordBytes = password.getBytes("UTF-8");  
  
            // 计算哈希值  
            byte[] hash = digest.digest(passwordBytes);  
  
            // 将哈希值转换为Base64字符串以便于存储和比较
            String res = Base64.getEncoder().encodeToString(hash);
            if(res.length()>30){
                res = res.substring(0,30);
            }
            return res;
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
//    public static void main(String[] args) {
//        String password = "myInsecurePassword";
//
//        // 直接对密码进行哈希处理（不使用盐值）
//        String hashedPassword = generateHashedPassword(password);
//
//        System.out.println("原始密码: " + password);
//        System.out.println("加密后的密码（哈希值）: " + hashedPassword);
//    }
}