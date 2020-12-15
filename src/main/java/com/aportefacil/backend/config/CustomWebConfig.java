package com.aportefacil.backend.config;

import com.aportefacil.backend.controllers.interceptors.SecurityInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CustomWebConfig implements WebMvcConfigurer {

    private final String frontendUrl;
    private final SecurityInterceptor securityInterceptor;

    public CustomWebConfig(@Value("${frontend.url}") String frontendUrl,
                           SecurityInterceptor securityInterceptor) {
        this.frontendUrl = frontendUrl;
        this.securityInterceptor = securityInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(this.frontendUrl)
                .allowCredentials(true)
                .allowedMethods("*")
                .allowedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login/**");
    }
}