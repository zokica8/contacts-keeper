package contacts.keeper.model;

import io.micronaut.security.authentication.providers.UserState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Login implements UserState {

    private String username;
    private String password;

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAccountExpired() {
        return false;
    }

    @Override
    public boolean isAccountLocked() {
        return false;
    }

    @Override
    public boolean isPasswordExpired() {
        return false;
    }
}
