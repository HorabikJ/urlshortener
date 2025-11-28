package pl.jacekhorabik.urlshortener.useraccount;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.springframework.stereotype.Service;
import pl.jacekhorabik.urlshortener.common.model.UserDataDTO;

@Service
@RequiredArgsConstructor
class UserAccountService {

  private final RealmResource urlShortenerRealm;

  void deleteUserAccount(final UserDataDTO userData) {
    UserResource userResource = urlShortenerRealm.users().get(userData.getUserId());

    // todo think about below methods, are they needed?
    // Verify user exists before deletion
    userResource.toRepresentation();
      // Delete the user
      userResource.remove();
  }
}
