package com.yang.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/project/agree/protocol/page").setViewName("project-agree");
        registry.addViewController("/project/launch/project/page").setViewName("project-launch");
        registry.addViewController("/project/return/info/page").setViewName("project-return");
        registry.addViewController("/project/create/confirm/page").setViewName("project-confirm");
        registry.addViewController("/project/create/success").setViewName("project-success");
        registry.addViewController("/project/show/detail").setViewName("project-detail");
    }
}
