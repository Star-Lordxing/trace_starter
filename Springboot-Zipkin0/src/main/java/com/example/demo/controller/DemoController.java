package com.example.demo.controller;

import com.dubbo.trace.configuration.TraceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Autowired
    private TraceConfig traceConfig;

    @RequestMapping(value = "/hellow")
    public String hellow() {
        System.out.println(traceConfig.getApplicationName());
        return "hellow";
    }

}
