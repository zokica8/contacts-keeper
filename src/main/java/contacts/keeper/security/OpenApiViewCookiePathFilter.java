package contacts.keeper.security;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.cookie.Cookie;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import org.reactivestreams.Publisher;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Requires(property = "micronaut.server.context-path")
@Filter(methods = {HttpMethod.GET, HttpMethod.HEAD}, patterns = {"/**/rapidoc*", "/**/redoc*", "/**/swagger-ui*"})
public class OpenApiViewCookiePathFilter implements HttpServerFilter {

    private final Cookie contextPathCookie;

    OpenApiViewCookiePathFilter(@Value("${micronaut.server.context-path}") String contextPath) {
        this.contextPathCookie = Cookie.of("contextPath",
                URLEncoder.encode(contextPath, StandardCharsets.UTF_8) + ";max-age=2;samesite");
    }

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        return Publishers.map(chain.proceed(request), response -> response.cookie(contextPathCookie));
    }
}
