package com.chen.service.impl;



import com.alibaba.fastjson.JSON;
import com.chen.entity.LoginUser;
import com.chen.entity.ResponseResult;
import com.chen.entity.User;
import com.chen.service.LoginServcie;
import com.chen.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginServcie {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public ResponseResult login(User user) {
        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //如果认证没通过，给出对应的提示
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("登录失败");
        }
        //如果认证通过了，使用userid生成一个永不过期jwt, jwt存入ResponseResult返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        Long usrId = loginUser.getUser().getId();
        String jwt = JwtUtil.createJWT(String.valueOf(usrId),315360000000L);
        //redis存储用户信息，并设置过期时间
        String loginUserJson = JSON.toJSONString(loginUser);
        redisTemplate.opsForValue().set("login:" + usrId,loginUserJson,30,TimeUnit.SECONDS);
        return new ResponseResult(200,"登录成功",jwt);
    }

    @Override
    public ResponseResult logout(String token) {
        //获取SecurityContextHolder中的用户id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
//        //删除redis中的值
//        redisCache.deleteObject("login:"+userid);
        return new ResponseResult(200,"注销成功");
    }
}
