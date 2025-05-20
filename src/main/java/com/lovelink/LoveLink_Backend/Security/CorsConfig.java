package com.lovelink.LoveLink_Backend.Security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // <- permite o Angular
                .allowedMethods("*")                     // <- permite todos os métodos (GET, POST, etc.)
                .allowedHeaders("*")                     // <- permite todos os headers
                .allowCredentials(false);                 // <- necessário se usa cookies/autenticação
    }
}
