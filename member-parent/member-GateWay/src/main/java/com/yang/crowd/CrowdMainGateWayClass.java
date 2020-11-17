package com.yang.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class CrowdMainGateWayClass {
    public static void main(String[] args) {
        SpringApplication.run(CrowdMainGateWayClass.class,args);
    }
}
