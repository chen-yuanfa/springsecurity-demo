package com.chen.controller;


import com.chen.entity.ResponseResult;
import com.chen.entity.User;
import com.chen.service.LoginServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private LoginServcie loginServcie;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        //登录
        return loginServcie.login(user);
    }
    @RequestMapping("/user/logout")
    public ResponseResult logout(String token){
        return loginServcie.logout(token);
    }
}
