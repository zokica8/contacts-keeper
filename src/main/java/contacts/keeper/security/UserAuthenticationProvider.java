package contacts.keeper.security;

import contacts.keeper.model.User;
import contacts.keeper.service.UserService;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.security.authentication.*;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;

@Requires(notEnv = Environment.TEST)
@Singleton
public class UserAuthenticationProvider implements AuthenticationProvider {

    @Inject
    private UserService userService;

    @Inject
    private PasswordEncoderConfig passwordEncoderConfig;

    @Override
    public Publisher<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {
        User user = userService.findByEmail(authenticationRequest.getIdentity().toString());
        if (authenticationRequest.getIdentity().toString().equals(user.getEmail()) &&
                passwordEncoderConfig.matches(authenticationRequest.getSecret().toString(), user.getPassword())) {
            UserDetails userDetails = new UserDetails(user.getFirstName(), Collections.singletonList("USER"));
            return Flowable.just(userDetails);
        }
        return Flowable.just(new AuthenticationFailed());
    }
}
