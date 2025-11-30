package pl.jacekhorabik.urlshortener.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@EnableWebSecurity
@Slf4j
class SecurityConfig {

  @Value("${keycloak.external-base-url}")
  private String keycloakExternalBaseURL;

  @Value("${app.external-base-url}")
  private String appExternalBaseUrl;

  @Bean
  SecurityFilterChain clientSecurityFilterChain(HttpSecurity http) throws Exception {
    http.oauth2Login(
        login -> {
          login.defaultSuccessUrl("/v1", true);
          login.failureHandler(
              (request, response, exception) -> {
                log.error("Login failed", exception);
                response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Authentication failed: " + exception.getMessage());
              });
        });
    http.logout(
        logout -> {
          // Use external URL for logout redirects
          String logoutUrl =
              String.format(
                  "%s/realms/urlshortener-keycloak-realm/protocol/openid-connect/logout",
                  keycloakExternalBaseURL);

          logout.logoutSuccessHandler(
              (request, response, authentication) -> {
                // Ensure this matches exactly what's configured in Keycloak
                String logoutSuccessRedirectUri = String.format("%s/v1", appExternalBaseUrl);

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
          requests.requestMatchers("/admin").hasAuthority(UserRole.ADMIN.toString());
          requests.requestMatchers("/user").hasAuthority(UserRole.USER.toString());
          requests.requestMatchers("/v1/account/**").hasAuthority(UserRole.USER.toString());
          requests.requestMatchers("/logout", "/logout/**").authenticated();
          requests
              .requestMatchers("/v1","/v1/**", "/actuator/health", "/favicon.ico", "/login/**")
              .permitAll();
          requests.anyRequest().denyAll();
        });

    // Disable CSRF for simplicity during testing - you might want to enable it later
    //      http.csrf(csrf -> csrf.disable());

    return http.build();
  }
}
