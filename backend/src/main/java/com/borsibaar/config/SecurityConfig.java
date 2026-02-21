package com.borsibaar.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpStatus;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        DefaultOAuth2AuthorizationRequestResolver defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository, "/api/oauth2/authorization");

        OAuth2AuthorizationRequestResolver customResolver = new OAuth2AuthorizationRequestResolver() {
            @Override
            public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
                var req = defaultResolver.resolve(request);
                if (req == null)
                    return null;
                return OAuth2AuthorizationRequest.from(req)
                        .additionalParameters(p -> p.put("prompt", "select_account"))
                        .build();
            }

            @Override
            public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
                var req = defaultResolver.resolve(request, clientRegistrationId);
                if (req == null)
                    return null;
                return OAuth2AuthorizationRequest.from(req)
                        .additionalParameters(p -> p.put("prompt", "select_account"))
                        .build();
            }
        };

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                // Add JWT authentication filter before standard authentication
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // Use IF_REQUIRED session management (stateless for API, sessions for OAuth2)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        // Allow OAuth2 endpoints and public routes
                        .requestMatchers("/", "/error", "/api/oauth2/**", "/api/login/oauth2/code/**", "/api/auth/login/success")
                        .permitAll()
                        // Public API endpoints
                        .requestMatchers(HttpMethod.GET, "/api/organizations/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/organizations").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/organizations/**").hasRole("ADMIN")
                        // Need to make these public for client page
                        // TODO: these should not be fully public
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/inventory/**").permitAll()
                        // All other API requests require authentication
                        .anyRequest().authenticated())
                // Return 401 for unauthenticated API requests instead of redirecting to OAuth
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .oauth2Login(oauth2 -> oauth2
                        .loginProcessingUrl("/api/login/oauth2/code/*")
                        .defaultSuccessUrl("/api/auth/login/success", true)
                        .authorizationEndpoint(auth -> auth
                                .baseUri("/api/oauth2/authorization")
                                .authorizationRequestResolver(customResolver)))
                .build();
    }

}
