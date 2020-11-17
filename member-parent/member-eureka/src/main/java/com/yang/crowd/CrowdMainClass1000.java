package com.yang.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class CrowdMainClass1000 {
    public static void main(String[] args) {
        SpringApplication.run(CrowdMainClass1000.class,args);
    }
}