package kz.bitlab.MainServiceProject.main.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Main Service API",
                version = "1.0",
                description = "API documentation for Main Service"
        )
)
@Configuration
public class SwaggerConfig {

}
