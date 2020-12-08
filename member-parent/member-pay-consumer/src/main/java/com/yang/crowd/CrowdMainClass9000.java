package com.yang.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class CrowdMainClass9000 {
    public static void main(String[] args) {
        SpringApplication.run(CrowdMainClass9000.class,args);
    }
}
