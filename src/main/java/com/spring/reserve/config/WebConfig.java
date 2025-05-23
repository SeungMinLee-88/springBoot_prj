package com.spring.reserve.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  private String resourcePath = "/upload/**";
  private String savePath = "file:///C:/Users/lsmls/IdeaProjects/springBoot_prj/attached";

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler(resourcePath)
            .addResourceLocations(savePath);
  }
}
