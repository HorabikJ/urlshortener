package pl.jacekhorabik.urlshortener.keycloak;

import jakarta.ws.rs.core.Response;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.jacekhorabik.urlshortener.common.security.UserRole;
import pl.jacekhorabik.urlshortener.keycloak.config.KeycloakProperties;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
class KeycloakController {

  private final Keycloak keycloak;
  private final KeycloakProperties keycloakProperties;

  @GetMapping("/register/{username}/{password}")
  int createUser(@PathVariable String username, @PathVariable String password) {
    RealmResource realm = keycloak.realm(keycloakProperties.getRealm());

    UserRepresentation user = new UserRepresentation();
    user.setEmail(username + "@onet.pl");
    user.setUsername(username);
    user.setEnabled(true);
    user.setEmailVerified(true);

    CredentialRepresentation userCredential = new CredentialRepresentation();
    userCredential.setType(CredentialRepresentation.PASSWORD);
    userCredential.setTemporary(false);
    userCredential.setValue(password);
    user.setCredentials(List.of(userCredential));

    try (Response response = realm.users().create(user)) {
      int status = response.getStatus();
      if (!HttpStatus.valueOf(status).is2xxSuccessful()) {
        return status;
      }
    }
    UserRepresentation createdUser = realm.users().search((user.getUsername())).get(0);
    UserResource userResource = realm.users().get(createdUser.getId());
    RoleRepresentation userRole = realm.roles().get(UserRole.USER.toString()).toRepresentation();
    userResource.roles().realmLevel().add(List.of(userRole));
    return 0;
  }
}
