package pl.jacekhorabik.urlshortener.useraccount;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UserAccountService {

  private final Keycloak keycloakAdminClient;

  @Value("${keycloak.realm}")
  private String realm;

  public void deleteCurrentUserAccount(Authentication authentication) {
    String userId = extractUserId(authentication);

    try {
      RealmResource realmResource = keycloakAdminClient.realm(realm);
      UserResource userResource = realmResource.users().get(userId);

      // Verify user exists before deletion
      userResource.toRepresentation();

      // Delete the user
      userResource.remove();

    } catch (NotFoundException e) {
      throw new RuntimeException("User not found in Keycloak", e);
    } catch (Exception e) {
      throw new RuntimeException("Failed to delete user account", e);
    }
  }

  /**
   * Extracts the user ID from the authentication object.
   *
   * @param authentication the authentication object
   * @return the user ID (subject claim from OIDC token)
   * @throws IllegalArgumentException if the user ID cannot be extracted
   */
  private String extractUserId(Authentication authentication) {
    if (authentication == null || authentication.getPrincipal() == null) {
      throw new IllegalArgumentException("Authentication is required");
    }

    Object principal = authentication.getPrincipal();

    if (principal instanceof OidcUser oidcUser) {
      String userId = oidcUser.getSubject();
      if (userId == null || userId.isBlank()) {
        throw new IllegalArgumentException("User ID not found in authentication token");
      }
      return userId;
    }

    throw new IllegalArgumentException(
        "Unsupported authentication type: " + principal.getClass().getName());
  }
}
