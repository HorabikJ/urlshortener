package pl.jacekhorabik.urlshortener.pages.useraccountpage;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.springframework.stereotype.Service;
import pl.jacekhorabik.urlshortener.common.dto.UserDataDTO;

@Service
@RequiredArgsConstructor
class UserAccountService {

  private final RealmResource urlShortenerRealm;

  void deleteUserAccount(final UserDataDTO userData) {
    UserResource userResource = urlShortenerRealm.users().get(userData.getUserId());

    // todo think about below methods, are they needed?
    // Verify user exists before deletion, throws new jakarta.ws.rs.NotFoundException
    userResource.toRepresentation();

    userResource.remove();
  }
}
