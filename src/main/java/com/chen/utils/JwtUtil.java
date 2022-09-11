package com.chen.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类
 */
public class JwtUtil {

    //有效期
    public static final Long JWT_TTL = 60 * 60 *1000L;// 60 * 60 *1000  一个小时
    //设置秘钥明文
    public static final String JWT_KEY = "123456";

    
    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     * @return
     */
    public static String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());// 设置过期时间
        return builder.compact();
    }

    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return
     */
    public static String createJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());// 设置过期时间
        return builder.compact();
    }

    /**
     * 创建token
     * @param id
     * @param subject
     * @return
     */
    public static String createJWT(String id, String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, id);// 设置过期时间
        return builder.compact();
    }

    /**
     * 创建token
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);// 设置过期时间
        return builder.compact();
    }

    public String refreshJWT(String jwt) throws Exception {
        Claims claims = parseJWT(jwt); // 获取荷载
        return createJWT(claims.getId(),claims.getSubject());
    }

    /**
     * 构建jwt
     * @param subject  主题内容
     * @param ttlMillis  过期时间
     * @param uuid  id
     * @return
     */
    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if(ttlMillis==null){
            ttlMillis= JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)              //唯一的ID
                .setSubject(subject)   // 主题  可以是JSON数据
                .setIssuedAt(now)      // 签发时间
                .signWith(SignatureAlgorithm.HS256, secretKey) //使用HS256对称加密算法签名, 第二个参数为秘钥
                .setExpiration(expDate);
    }

    /**
     * 解析jwtToken获取加密内容
     * @param jwt
     * @return
     */
    public static String getSubject(String jwt) throws Exception {
        return parseJWT(jwt).getSubject();
    }

    /**
     * 判断 jwt 是否失效
     *
     * @param jwt
     * @return
     */
    private boolean isTokenExpired(String jwt) {
        try {
            Claims claims = parseJWT(jwt);
            Date expiration = claims.getExpiration();  // 获取 token 失效时间
            return expiration.before(new Date()); // 如果 token 过期时间在当前时间前面，有效
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param jwt
     * @param subject
     * @return
     */
    public boolean validateToken(String jwt, String subject) {
        if (jwt==null){
            throw new RuntimeException("JWT令牌不能为空！");
        }
        if (subject==null){
            throw new RuntimeException("校验内容不能为空！");
        }
        try {
            Claims claims = parseJWT(jwt);
            String s = claims.getSubject();
            Date expiration = claims.getExpiration();
            return expiration.before(new Date())&&subject.equals(claims.getSubject());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
    /**
     * 解析
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

    /**
     * 生成加密后的秘钥 secretKey
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void main(String[] args) throws Exception {
//        String jwt = createJWT("2123");
//        System.out.println(jwt);
        String subject = getSubject("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJkNjY5ZWEyODUyZmQ0MWZhODA1YTVhZjdiODI1NDY4ZCIsInN1YiI6IjIxMjMiLCJpYXQiOjE2NjI3MzUzMjAsImV4cCI6MTY2MjczODkyMH0.8BUltYOa3jd7A6y1LV9sJAoUt6LiftL6KOwrezA6sQY");
        System.out.println(subject);
    }

}