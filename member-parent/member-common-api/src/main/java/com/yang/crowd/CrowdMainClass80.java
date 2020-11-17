package com.yang.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
@EnableEurekaClient
@RibbonClients({@RibbonClient(name = "YANG-CROWD-MYSQL"),
                @RibbonClient(name = "YANG-CROWD-REDIS")})
public class CrowdMainClass80 {
    public static void main(String[] args) {
        SpringApplication.run(CrowdMainClass80.class,args);
    }
}
