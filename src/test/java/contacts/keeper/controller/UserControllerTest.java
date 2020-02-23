package contacts.keeper.controller;

import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import contacts.keeper.model.User;
import contacts.keeper.service.UserService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class UserControllerTest {

    @Inject
    EmbeddedServer embeddedServer;

    @Test
    public void whenAccessingUnsecuredURLAccessThatURL() throws Exception {
        try (RxHttpClient client =
                     embeddedServer.getApplicationContext().createBean(RxHttpClient.class, embeddedServer.getURL())) {
            assertEquals(HttpStatus.OK, client.toBlocking().exchange("/api/users/1").status());
        }
    }

    @Test
    public void loginWithRightCredentials() throws Exception{
        try(RxHttpClient client =
                embeddedServer.getApplicationContext().createBean(RxHttpClient.class, embeddedServer.getURL())) {
            UsernamePasswordCredentials credentials =
                    new UsernamePasswordCredentials("zokivasilic8@gmail.com", "eraojdanic55");
            HttpRequest<?> request = HttpRequest.POST("http://localhost:8080/login", credentials);
            HttpResponse<BearerAccessRefreshToken> response = client.toBlocking().exchange(request, BearerAccessRefreshToken.class);
            assertEquals(HttpStatus.OK, response.status());
            assertEquals("Zoran", Objects.requireNonNull(response.body()).getUsername());
            String accessToken = Objects.requireNonNull(response.body()).getAccessToken();
            assertTrue(JWTParser.parse(accessToken) instanceof SignedJWT);
            String refreshToken = Objects.requireNonNull(response.body()).getRefreshToken();
            assertTrue(JWTParser.parse(refreshToken) instanceof SignedJWT);
        }
    }
}
