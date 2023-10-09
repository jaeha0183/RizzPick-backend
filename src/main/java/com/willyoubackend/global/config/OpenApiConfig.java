package com.willyoubackend.global.config;

import com.willyoubackend.domain.user.jwt.JwtUtil;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Configuration
public class OpenApiConfig {
    @Value("${willyou.openapi.local-url}")
    private String localUrl;
    @Value("${willyou.openapi.deploy-url}")
    private String deployUrl;

    @Bean
    public OpenAPI willYouOpenApi() {
        // Local
        Server localServer = new Server();
        localServer.setUrl(localUrl);
        localServer.setDescription("Local Server");

        // Deploy
        Server deployServer = new Server();
        deployServer.setUrl(deployUrl);
        deployServer.setDescription("Deploy Server");

        Contact contact = new Contact();
        contact.setEmail("willyouadmin@gmail.com");
        contact.setName("WillYou Admin");
        contact.setUrl("https://www.naver.com");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("WillYou API Board")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage WillYou API.").termsOfService("https://www.scourt.go.kr/scourt/index.html")
                .license(mitLicense);

        // Auth
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("Set-Cookie");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("Set-Cookie");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Set-Cookie", securityScheme))
                .addSecurityItem(securityRequirement)
                .info(info)
                .servers(List.of(localServer, deployServer));
    }
}
