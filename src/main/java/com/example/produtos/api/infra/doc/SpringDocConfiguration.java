package com.example.produtos.api.infra.doc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes("bearer-key",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
            .info(new Info()
                .title("API de Exemplo Desenvolvida para as Aulas de TADS")
                .description("API Rest da aplicação tads_produtos_springboot_exemplo, contendo as funcionalidades de CRUD de produtos.")
                .contact(new Contact()
                    .name("Time Aulas TADS")
                    .email("tads@ifsul.edu.br"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("http://voll.med/api/licenca")));
    }
}
