package pl.jacekhorabik.urlshortener.keycloak;

import jakarta.ws.rs.core.Response;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.jacekhorabik.urlshortener.common.security.UserRole;

@Service
@RequiredArgsConstructor
class KeycloakService {

  private final RealmResource urlshortenerRealm;

  void registerUser(UserDTO userDTO) throws KeycloakUserCreationException {
    UserRepresentation user = new UserRepresentation();
    user.setEmail(userDTO.email());
    user.setUsername(userDTO.username());
    user.setEnabled(true);
    user.setEmailVerified(true);

    CredentialRepresentation userCredential = new CredentialRepresentation();
    userCredential.setType(CredentialRepresentation.PASSWORD);
    userCredential.setTemporary(false);
    userCredential.setValue(userDTO.password());
    user.setCredentials(List.of(userCredential));

    // maybe validate username, email and password in frontend so then we would not have to throw
    // exceptions from keycloak response?
    try (Response response = urlshortenerRealm.users().create(user)) {
      int status = response.getStatus();
      if (!HttpStatus.valueOf(status).is2xxSuccessful()) {
        throw new KeycloakUserCreationException("exception", HttpStatus.valueOf(status));
      }
    }
    UserRepresentation createdUser = urlshortenerRealm.users().search((user.getUsername())).get(0);
    UserResource userResource = urlshortenerRealm.users().get(createdUser.getId());
    RoleRepresentation userRole =
        urlshortenerRealm.roles().get(UserRole.USER.toString()).toRepresentation();
    userResource.roles().realmLevel().add(List.of(userRole));
  }
}

// the same username -> 409 conflict
// the same email -> 409 conflict
