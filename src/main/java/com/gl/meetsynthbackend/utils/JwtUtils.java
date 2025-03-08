package com.gl.meetsynthbackend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {

    // 密钥
    private static final String SECRET_KEY = "NDUyMGds";

    // 过期时间，12小时（毫秒）
    private static final long EXPIRATION_TIME = 12 * 60 * 60 * 1000;

    /**
     * 生成JWT令牌
     *
     * @param dataMap 要包含在令牌中的数据
     * @return 生成的JWT令牌
     */
    public static String generateToken(Map<String, Object> dataMap) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .addClaims(dataMap)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
    }

    /**
     * 解析JWT令牌
     *
     * @param token 要解析的JWT令牌
     * @return 解析后的Claims对象
     */
    public static Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }
}
