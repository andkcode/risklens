package com.risklens.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI riskLensOpenAPI() {
    final String securitySchemeName = "bearerAuth";

    return new OpenAPI()
        // 1. Info
        .info(
            new Info()
                .title("RiskLens API")
                .description("Fintech Investment Portfolio Management & Risk Analysis Platform API")
                .version("1.0.0"))
        // 2. Servers
        .servers(
            List.of(
                new Server().url("http://localhost:8080").description("Local Development Server")))
        // 3. Global Bearer Auth (JWT) Security Requirement
        .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
        .components(
            new Components()
                .addSecuritySchemes(
                    securitySchemeName,
                    new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description(
                            "Provide the JWT token prefixed with Bearer, e.g., 'Bearer eyJhbGciOi...'")))
        // 4. Tags
        .tags(
            List.of(
                new Tag().name("Auth").description("Authentication and token management endpoints"),
                new Tag()
                    .name("Portfolios")
                    .description("Investment portfolio management endpoints"),
                new Tag().name("Positions").description("Portfolio asset positions management"),
                new Tag()
                    .name("Risk Assessments")
                    .description("Risk calculation and analysis endpoints"),
                new Tag()
                    .name("Market Data")
                    .description("Asset pricing and historical market data")));
  }
}
