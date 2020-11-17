package com.yang.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class CrowdMainClass2000 {
    public static void main(String[] args) {
        SpringApplication.run(CrowdMainClass2000.class,args);
    }
}
