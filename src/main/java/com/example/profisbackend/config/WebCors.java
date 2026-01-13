package com.example.profisbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
@EnableWebMvc // Documentation https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-config/enable.html
public class WebCors implements WebMvcConfigurer{
     @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        .allowedMethods("*")
        .allowedOriginPatterns("*");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // This maps all URL patterns to the 'static' folder
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(0); // Disable caching for testing
    }
}

