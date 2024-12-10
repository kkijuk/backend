package umc.kkijuk.server.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {


    @Value("${api.server.url}")
    private String serverUrl;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(info)
                .addServersItem(new Server().url(serverUrl));
    }

    Info info = new Info().title("KKIJUK Backend APIS").version("0.0.1").description(
            "<h3>KKIJUK Backend APIS</h3>");
}
