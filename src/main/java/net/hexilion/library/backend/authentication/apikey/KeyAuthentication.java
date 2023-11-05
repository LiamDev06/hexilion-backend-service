package net.hexilion.library.backend.authentication.apikey;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Represents an authentication token for the API key-based authentication.
 */
public class KeyAuthentication extends AbstractAuthenticationToken {

    private final @NonNull String apiKey;

    public KeyAuthentication(@NonNull String apiKey, @NonNull Collection<? extends GrantedAuthority> authorities) {
        super(authorities);

        this.apiKey = apiKey;
        this.setAuthenticated(true);
    }

    @Override
    public @Nullable Object getCredentials() {
        return null;
    }

    @Override
    public @NonNull Object getPrincipal() {
        return this.apiKey;
    }
}