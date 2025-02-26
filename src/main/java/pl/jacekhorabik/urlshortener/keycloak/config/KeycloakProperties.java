package pl.jacekhorabik.urlshortener.keycloak.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "keycloak-admin-client")
class KeycloakProperties {

  private String realm;
  private String clientId;
  private String username;
  private String password;
  private String url;
}
