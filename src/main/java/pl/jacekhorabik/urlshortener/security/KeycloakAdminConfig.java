package pl.jacekhorabik.urlshortener.security;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class KeycloakAdminConfig {

  @Value("${keycloak.external-base-url}")
  private String keycloakServerUrl;

  @Value("${keycloak.realm}")
  private String realm;

  @Value("${keycloak.admin-username}")
  private String adminUsername;

  @Value("${keycloak.admin-password}")
  private String adminPassword;

  @Bean
  public Keycloak keycloakAdminClient() {
    log.info(
        "Initializing Keycloak Admin Client for server: {}, realm: {}", keycloakServerUrl, realm);
    return KeycloakBuilder.builder()
        .serverUrl(keycloakServerUrl)
        .realm("master") // Admin client authenticates against master realm
        .username(adminUsername)
        .password(adminPassword)
        .clientId("admin-cli")
        .build();
  }
}
