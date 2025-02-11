package pl.jacekhorabik.urlshortener.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
class SecurityConfig {

  //todo add csrf if it is not enabled by default
  @Bean
  SecurityFilterChain clientSecurityFilterChain(
      HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository)
      throws Exception {
    http.oauth2Login(Customizer.withDefaults());
    http.logout(
        (logout) -> {
          final var logoutSuccessHandler =
              new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
          logoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/");
          logout.logoutSuccessHandler(logoutSuccessHandler);
        });

    http.authorizeHttpRequests(
        requests -> {
          requests.requestMatchers("/", "/favicon.ico").permitAll();
          requests.requestMatchers("/nice").hasAuthority("NICE");
          requests.anyRequest().denyAll();
        });
    return http.build();
  }
}
