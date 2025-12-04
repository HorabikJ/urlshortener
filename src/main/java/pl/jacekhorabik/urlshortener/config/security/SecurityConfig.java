package pl.jacekhorabik.urlshortener.config.security;

import jakarta.servlet.http.HttpServletResponse;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.util.UriComponentsBuilder;
import pl.jacekhorabik.urlshortener.pages.common.view.View;

@Configuration
@EnableWebSecurity
@Slf4j
class SecurityConfig {

  @Value("${keycloak.external-base-url}")
  private URL keycloakExternalBaseURL;

  @Value("${keycloak.clientId}")
  private String urlshortenerKeycloakClientId;

  @Value("${app.external-base-url}")
  private URL appExternalBaseUrl;

  @Bean
  SecurityFilterChain clientSecurityFilterChain(final HttpSecurity http) throws Exception {
    http.oauth2Login(
        login -> {
          login.defaultSuccessUrl(View.MAIN_PAGE.getViewPath(), true);
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
          final String logoutUrl =
              String.format(
                  "%s/realms/urlshortener-keycloak-realm/protocol/openid-connect/logout",
                  keycloakExternalBaseURL);
          // Ensure this matches exactly what's configured in Keycloak
          final String logoutSuccessRedirectUri =
              appExternalBaseUrl.toString() + View.MAIN_PAGE.getViewPath();

          logout.logoutSuccessHandler(
              (request, response, authentication) -> {
                response.sendRedirect(
                    UriComponentsBuilder.fromUriString(logoutUrl)
                        .queryParam("post_logout_redirect_uri", logoutSuccessRedirectUri)
                        .queryParam("client_id", urlshortenerKeycloakClientId)
                        .build()
                        .toUriString());
              });
          // Configure logout URL
          logout.logoutUrl("/logout");
          // Allow the iframe to access the logout URL in case it's in an iframe
          logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
        });

    http.exceptionHandling(
        exceptionHandlingConfigurer ->
            exceptionHandlingConfigurer.accessDeniedHandler(
                (request, response, accessDeniedException) -> {
                  log.warn(
                      "Access denied exception for path: {}", request.getRequestURI(), accessDeniedException);
                  response.sendRedirect(View.NOT_FOUND.getViewPath());
                }));

    http.authorizeHttpRequests(
        requests -> {
          requests.requestMatchers("/admin").hasAuthority(UserRole.ADMIN.toString());
          requests
              .requestMatchers("/user", "/v1/account/**", "/v1/url/delete")
              .hasAuthority(UserRole.USER.toString());
          requests.requestMatchers("/logout", "/logout/**").authenticated();
          requests
              .requestMatchers(
                  "/v1",
                  "/v1/r/**",
                  "/v1/url/create",
                  "/v1/not-found",
                  "/login/**",
                  "/css/**",
                  "/favicon.ico",
                  "/favicon.svg")
              .permitAll();
          requests.anyRequest().denyAll();
        });

    // Disable CSRF for simplicity during testing - you might want to enable it later
    //      http.csrf(csrf -> csrf.disable());

    return http.build();
  }
}
