package com.example.demo.controller;

import com.dubbo.demo.api.DemoRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ZipkinController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DemoRpcService demoRpcService;

    @GetMapping("/service0")
    public String service() throws Exception {
        Thread.sleep(100);
        Map map = new HashMap<>();
        map.put("name", "我是rest请求！");
        ResponseEntity<String> response = restTemplate.postForEntity("http://192.168.1.100:8081/service1", map, String.class);
        return response.getBody();
    }


    @GetMapping("/testDubbo")
    public String testDubbo() throws Exception {
        System.out.println("接受http请求：");
        Thread.sleep(1000);
        System.out.println("发起RPC调用");
        String name = demoRpcService.getUserName("dubbo1");
        System.out.println("处理RPC调用返回结果：" + name);
        Thread.sleep(1500);

        System.out.println("发起restTemplate:");
        Map map = new HashMap<>();
        map.put("name", "我是rest请求！");
        String response = restTemplate.getForObject("http://192.168.1.100:8081/service1", String.class, map);
        System.out.println("接受testTemplate:" + response);
        return response;
    }
}
