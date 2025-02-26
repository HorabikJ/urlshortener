package pl.jacekhorabik.urlshortener.keycloak;

import org.springframework.http.HttpStatus;

class PasswordTooWeakException extends KeycloakUserCreationException {

  public PasswordTooWeakException(String message, HttpStatus httpStatus) {
    super(message, httpStatus);
  }
}
