package pl.jacekhorabik.urlshortener.keycloak;

import org.springframework.http.HttpStatus;

class KeycloakUserCreationException extends Exception {

  private final HttpStatus httpStatus;

  public KeycloakUserCreationException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }
}
