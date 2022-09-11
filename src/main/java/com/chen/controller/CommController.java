package com.chen.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comm")
public class CommController {
    @GetMapping("/hello")
    public String hello(){
        return "comm--->hello world";
    }
}

