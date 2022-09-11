package com.chen.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/hello")
//    @PreAuthorize("hasAuthority('admin')")
//    @PreAuthorize("hasAnyAuthority('admin','test','system:dept:list')")
//    @PreAuthorize("hasRole('system:dept:list')")
//    @PreAuthorize("hasAnyRole('admin','system:dept:list')")
    public String hello(){
        return "hello world";
    }



}
