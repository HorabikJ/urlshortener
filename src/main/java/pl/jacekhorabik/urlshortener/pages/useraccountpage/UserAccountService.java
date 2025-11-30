package pl.jacekhorabik.urlshortener.pages.useraccountpage;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.jacekhorabik.urlshortener.pages.common.dto.UserDataDTO;
import pl.jacekhorabik.urlshortener.pages.common.entity.UrlRepository;

@Service
@RequiredArgsConstructor
class UserAccountService {

  private final RealmResource urlShortenerRealm;
  private final UrlRepository urlRepository;

  @Transactional // todo - find out how this annotation works with repository and realm resource,
  // is it really needed over here?
  void deleteUserAccount(final UserDataDTO userData) {
    urlRepository.deleteUrlEntityByUserId(userData.getUserId());

    UserResource userResource = urlShortenerRealm.users().get(userData.getUserId());

    // todo think about below methods, are they needed?
    // Verify user exists before deletion, throws new jakarta.ws.rs.NotFoundException
    userResource.toRepresentation();

    userResource.remove();
  }
}
