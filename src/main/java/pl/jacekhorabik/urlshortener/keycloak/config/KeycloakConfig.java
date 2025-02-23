package pl.jacekhorabik.urlshortener.keycloak.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakConfig {

  @Bean
  public Keycloak keycloak(KeycloakProperties properties) {
    return KeycloakBuilder.builder()
        .grantType(OAuth2Constants.PASSWORD)
        .realm(properties.getRealm())
        .clientId(properties.getClientId())
        .username(properties.getUsername())
        .password(properties.getPassword())
        .serverUrl(properties.getUrl())
        .build();
  }
}
