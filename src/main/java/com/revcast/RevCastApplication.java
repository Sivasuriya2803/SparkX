package com.revcast;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RevCastApplication {

    public static void main(String[] args) {
        SpringApplication.run(RevCastApplication.class, args);
    }

    /**
     * Configure OpenAPI (Swagger) documentation
     */
    @Bean
    public OpenAPI revcastOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RevCast API")
                        .description("Enterprise Grade Revenue Forecasting System")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("RevCast Team")
                                .email("support@revcast.com")
                                .url("https://revcast.com")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token for authentication")));
    }
}

