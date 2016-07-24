package com.itest.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 第一个SpringBoot RestController
 */
@RestController
@EnableAutoConfiguration
public class HelloWorldController {

    @RequestMapping("/")
    public String hello() {
        return "Hello iTest with Spring Boot.";
    }

    @RequestMapping("/tt")
    public String test() {
        return "基于Spring Boot构建!!! @TAKA";
    }


}