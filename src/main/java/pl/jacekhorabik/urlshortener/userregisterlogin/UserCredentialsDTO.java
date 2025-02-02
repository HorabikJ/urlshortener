package pl.jacekhorabik.urlshortener.userregisterlogin;

record UserCredentialsDTO(String email, String password) {

  UserCredentialsDTO() {
    this(null, null);
  }
}
