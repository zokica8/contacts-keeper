package contacts.keeper.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import javax.inject.Inject;
import java.util.Objects;

@MicronautTest
public class ContactsControllerTest {

    @Inject
    EmbeddedServer embeddedServer;

    @Test
    public void whenAccessingSecureURLWithoutAuthenticatingThrowUnauthorized() throws Exception{
        try(RxHttpClient client =
                    embeddedServer.getApplicationContext().createBean(RxHttpClient.class, embeddedServer.getURL())) {
            assertThrows(HttpClientResponseException.class, () -> {
                int code = client.toBlocking().exchange("/api/contacts/1").code();
                assertEquals(401, code);
            });
        }
    }

    @Test
    public void whenAuthenticatedGoToTheURL() {
        try(RxHttpClient client =
                    embeddedServer.getApplicationContext().createBean(RxHttpClient.class, embeddedServer.getURL())) {
            UsernamePasswordCredentials credentials =
                    new UsernamePasswordCredentials("zokivasilic8@gmail.com", "eraojdanic55");
            HttpRequest<?> request = HttpRequest.POST("http://localhost:8080/login", credentials);
            HttpResponse<BearerAccessRefreshToken> response = client.toBlocking().exchange(request, BearerAccessRefreshToken.class);

            assertEquals(200, response.code());

            String accessToken = Objects.requireNonNull(response.body()).getAccessToken();
            HttpRequest<?> requestWithAuthorization = HttpRequest.GET("/api/contacts/1").bearerAuth(accessToken);
            HttpResponse<String> authorizedResponse = client.toBlocking().exchange(requestWithAuthorization, String.class);
            assertEquals(HttpStatus.OK, authorizedResponse.status());
            assertNotNull(authorizedResponse.body());
        }
    }
}
