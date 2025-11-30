package pl.jacekhorabik.urlshortener.pages.useraccountpage;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class KeycloakAdminConfig {

  @Value("${keycloak.realm}")
  private String realm;

  @Value("${keycloak.internal-base-url}")
  private String keycloakServerUrl;

  @Value("${keycloak.admin-username}")
  private String adminUsername;

  @Value("${keycloak.admin-password}")
  private String adminPassword;

  @Bean
  Keycloak keycloakAdminClient() {
    return KeycloakBuilder.builder()
        .serverUrl(keycloakServerUrl)
        .realm("master") // Admin client authenticates against master realm
        .username(adminUsername)
        .password(adminPassword)
        .clientId("admin-cli")
        .build();
  }

  @Bean
  RealmResource urlShortenerRealm(Keycloak keycloakAdminClient) {
    return keycloakAdminClient.realm(realm);
  }
}
