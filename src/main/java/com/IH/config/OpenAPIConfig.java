package com.IH.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.validation.constraints.Email;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "IfoHub",
                description = "API fo ...",
                version = "1.0.0",
                contact = @Contact(
                        name = "Evgeny Makushev",
                        email = "wrewrwerwe@mail.ru")
        ),
        servers = {@Server(url = "http://localhost:8080")}
)
public class OpenAPIConfig {//можно сделать через бины!!!!!!! убрать аннотации сверху, позволит добавлять JWT
}
