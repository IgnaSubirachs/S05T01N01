package cat.itacademy.s05.t01.n01.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Blackjack API - Spring WebFlux",
                version = "1.0.0",
                description = "Reactive REST API for playing Blackjack. Connects MongoDB and MySQL using R2DBC and Spring WebFlux.",
                contact = @Contact(name = "Ignasi Subirachs"),
                license = @License(name = "MIT License")
        ),
        servers = {
                @Server(
                        url = "https://blackjackapi-ignasisubirachs.onrender.com",
                        description = "Render Server"
                )
        }
)

public class OpenApiConfig {

}
