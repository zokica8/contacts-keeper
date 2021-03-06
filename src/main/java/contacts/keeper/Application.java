package contacts.keeper;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@SecurityScheme(name = "BearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "jwt")
@OpenAPIDefinition(
    info = @Info(
            title = "contacts-keeper",
            version = "1.0",
            description = "Contacts Keeper API"
    )
)
@Server(url = "http://localhost:8080", description = "base url for the Contacts Keeper API")
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }
}