package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ZipkinController {

    @GetMapping("/service1")
    public String service(String name) throws Exception {
        System.out.println("接受restTemplate:" + name);
        Thread.sleep(1000);
        return "good!";
    }
}
