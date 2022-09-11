package com.chen.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/outcomm")
public class OutCommController {
    @GetMapping("/hello")
    public String hello(){
        return "outcomm--->hello world";
    }
}

