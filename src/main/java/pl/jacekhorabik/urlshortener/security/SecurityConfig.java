package pl.jacekhorabik.urlshortener.security;

import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@EnableWebSecurity
class SecurityConfig {

  // todo add csrf if it is not enabled by default
  @Bean
  SecurityFilterChain clientSecurityFilterChain(
      @NotNull HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository)
      throws Exception {
    http.oauth2Login(
        login -> {
          login.defaultSuccessUrl("/v1/", true);
          login.failureHandler(
              (request, response, exception) -> {
                exception.printStackTrace();
                response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Authentication failed: " + exception.getMessage());
              });
        });
    http.logout(
        logout -> {
          // Use external URL for logout redirects
          String logoutUrl =
              "http://localhost:30009/realms/urlshortener-keycloak-realm/protocol/openid-connect/logout";

          logout.logoutSuccessHandler(
              (request, response, authentication) -> {
                // Ensure this matches exactly what's configured in Keycloak
                String logoutSuccessRedirectUri = "http://localhost:30008/v1/";

                String logoutRedirectUrl =
                    UriComponentsBuilder.fromUriString(logoutUrl)
                        .queryParam("post_logout_redirect_uri", logoutSuccessRedirectUri)
                        .queryParam("client_id", "urlshortener-keycloak-client")
                        .build()
                        .toUriString();

                response.sendRedirect(logoutRedirectUrl);
              });
          // Configure logout URL
          logout.logoutUrl("/logout");
          // Allow the iframe to access the logout URL in case it's in an iframe
          logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
        });

    //    todo think how to authorize k8s probes to hit /actuator/health endpoints when they will be
    // secured
    http.authorizeHttpRequests(
        requests -> {
          // Permit access to these endpoints for everyone
          requests
              .requestMatchers("/v1/**", "/actuator/health", "/favicon.ico", "/login/**")
              .permitAll();
          // Permit access to logout for authenticated users
          requests.requestMatchers("/logout", "/logout/**").authenticated();
          // Role-specific endpoints
          requests.requestMatchers("/admin").hasAuthority("ADMIN");
          requests.requestMatchers("/user").hasAuthority("USER");
          // Deny all other requests
          requests.anyRequest().denyAll();
        });

    // Disable CSRF for simplicity during testing - you might want to enable it later
    //      http.csrf(csrf -> csrf.disable());

    return http.build();
  }
}
