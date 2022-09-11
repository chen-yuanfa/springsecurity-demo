package com.chen.service;

import com.chen.entity.ResponseResult;
import com.chen.entity.User;


public interface LoginServcie {
    ResponseResult login(User user);

    ResponseResult logout(String token);
}
