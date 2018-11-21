package com.example.demo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({"classpath:*dubbo*.xml"})
@ComponentScan(basePackages = "com.dubbo.trace")
public class DubboConfig {

}
