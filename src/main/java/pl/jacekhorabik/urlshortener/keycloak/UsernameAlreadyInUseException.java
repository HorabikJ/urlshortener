package pl.jacekhorabik.urlshortener.keycloak;

import org.springframework.http.HttpStatus;

class UsernameAlreadyInUseException extends KeycloakUserCreationException {

  public UsernameAlreadyInUseException(String message, HttpStatus httpStatus) {
    super(message, httpStatus);
  }
}
