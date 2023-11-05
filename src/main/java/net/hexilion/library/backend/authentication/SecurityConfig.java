package net.hexilion.library.backend.authentication;

import net.hexilion.library.backend.authentication.apikey.CorrectKeyProvider;
import net.hexilion.library.backend.ApplicationConfiguration;
import net.hexilion.library.backend.exception.InvalidAPIKeyPathException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for security settings and filter chain.
 * Used to provide authentication for the API key.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final @NonNull AuthenticationService authenticationService;

    @Autowired
    public SecurityConfig(@NonNull ApplicationConfiguration configurationProperties) {
        String apiKeyPath = configurationProperties.getApiKeyPath();
        if (apiKeyPath == null) {
            throw new InvalidAPIKeyPathException("null", "The API key path is null! The SecurityConfig could initialize properly...");
        }

        CorrectKeyProvider correctKeyProvider = new CorrectKeyProvider(apiKeyPath);
        this.authenticationService = new AuthenticationService(configurationProperties, correctKeyProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(@NonNull HttpSecurity http) throws Exception {
        // Disable the default
        http.csrf(AbstractHttpConfigurer::disable);

        // Authorize all requests and authenticated requests
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());

        // HTTP basic
        http.httpBasic(httpBasic -> httpBasic.init(http));

        // Session
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add our custom authentication filter that uses the API keys
        http.addFilterBefore(new AuthenticationFilter(this.authenticationService), UsernamePasswordAuthenticationFilter.class);

        // Build the HTTP
        return http.build();
    }
}