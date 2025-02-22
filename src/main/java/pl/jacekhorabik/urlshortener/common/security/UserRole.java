package pl.jacekhorabik.urlshortener.common.security;

public enum UserRole {
  ADMIN("ADMIN"),
  USER("USER");

  private final String role;

  UserRole(String role) {
    this.role = role;
  }

  @Override
  public String toString() {
    return role;
  }
}
