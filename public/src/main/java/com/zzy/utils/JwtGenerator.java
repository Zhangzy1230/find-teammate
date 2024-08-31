package com.zzy.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtGenerator {
    private final static String SECRET_KEY = "zzy";
    private static final String ISSUER = "zzy";

    //根据username创建一个jwt
    public static String createToken(String username) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        String token = JWT.create()
                .withIssuer(ISSUER) // 设置发行人
                .withSubject("User Authentication") // 设置主题
                .withClaim("username", username) // 添加自定义字段，这里添加用户名
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 设置过期时间，这里设置为1天
                .sign(algorithm); // 签名
        return token;
    }
    //根据令牌解析用户名
    public static String parseUsername(String token) throws Exception{
        // 创建一个JWT验证器
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER) // 设置期望的发行人，如果JWT中没有这个字段或者不匹配，验证将失败
                .build();
        // 解码并验证JWT
        DecodedJWT decodedJWT = verifier.verify(token);
        // 提取用户名
        return decodedJWT.getClaim("username").asString();
    }
}
