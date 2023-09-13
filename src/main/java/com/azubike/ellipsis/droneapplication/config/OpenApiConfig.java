package com.azubike.ellipsis.droneapplication.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Enu Richard Azubike",
                        email = "enuazubike88@gmail.com",
                        url = "https://github.com/azubikeenu/Musala_Assignment"
                ),
                description = "Documentation for drone application",
                title = "Drone Application Documentation",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "local ENV ",
                        url = "http://localhost:8080"
                )
        }
)

public class OpenApiConfig {
}
