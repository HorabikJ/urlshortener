package pl.jacekhorabik.urlshortener.keycloak;

record UserDTO(String username, String email, String password) {

  UserDTO() {
    this(null, null, null);
  }
}
