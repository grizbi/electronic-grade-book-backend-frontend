package com.example.electronicgradebook.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/WEB-INF/view/react-app/build/static/");
        registry.addResourceHandler("/*.js")
                .addResourceLocations("/WEB-INF/view/react-app/build/");
        registry.addResourceHandler("/*.json")
                .addResourceLocations("/WEB-INF/view/react-app/build/");
        registry.addResourceHandler("/*.ico")
                .addResourceLocations("/WEB-INF/view/react-app/build/");
        registry.addResourceHandler("/index.html")
                .addResourceLocations("/WEB-INF/view/react-app/build/index.html");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
    }
}