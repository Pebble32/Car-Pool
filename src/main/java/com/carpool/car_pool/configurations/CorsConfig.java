package com.carpool.car_pool.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for setting up Cross-Origin Resource Sharing (CORS) settings.
 */
@Configuration
public class CorsConfig {

    @Value("${CORS_ALLOWED_ORIGINS:http://localhost:3000}")
    private String[] allowedOrigins;

    /**
     * Configures CORS mappings to allow specified origins and HTTP methods.
     *
     * @return A {@link WebMvcConfigurer} instance with customized CORS settings.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry){
                registry.addMapping("/**")
                        .allowedOrigins(allowedOrigins)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
