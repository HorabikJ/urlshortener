package pl.jacekhorabik.urlshortener.keycloak;

import jakarta.ws.rs.core.Response;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.*;
import pl.jacekhorabik.urlshortener.common.security.UserRole;
import pl.jacekhorabik.urlshortener.keycloak.config.KeycloakProperties;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class KeycloakController {

  private final Keycloak keycloak;
  private final KeycloakProperties keycloakProperties;

  @GetMapping("/register/{username}/{password}")
  int createUser(@PathVariable String username, @PathVariable String password) {
    RealmResource realm = keycloak.realm(keycloakProperties.getRealm());

    UserRepresentation user = new UserRepresentation();
    user.setEmail(username + "@onet.pl");
    user.setUsername(username);
    user.setRealmRoles(List.of(UserRole.USER.toString()));
    user.setEnabled(true);
    user.setEmailVerified(true);

    CredentialRepresentation userCredential = new CredentialRepresentation();
    userCredential.setType(CredentialRepresentation.PASSWORD);
    userCredential.setTemporary(false);
    userCredential.setValue(password);

    user.setCredentials(List.of(userCredential));

    try (Response response = realm.users().create(user)) {
      int status = response.getStatus();
      System.out.println("after response");
      return status;
    }
  }
}
