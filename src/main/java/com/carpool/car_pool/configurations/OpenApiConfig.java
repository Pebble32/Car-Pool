package com.carpool.car_pool.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up OpenAPI documentation.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configures the OpenAPI documentation for the CarPool API.
     *
     * @return An {@link OpenAPI} instance with specified API information.
     */
    @Bean
    public OpenAPI carPoolOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("CarPool API")
                        .description("API documentation for CarPool application")
                        .version("1.0"));
    }
}
