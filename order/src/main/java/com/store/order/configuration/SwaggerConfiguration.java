package com.store.order.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration // Import the Swagger configuration
@OpenAPIDefinition(info = @Info(title = "Order", description = "Order Microservice", version = "v1", license = @License(name = "MIT", url = "http://localhost"))) // Swagger API configuration
public class SwaggerConfiguration {
}
