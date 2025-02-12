package pl.jacekhorabik.urlshortener.common.security;

public enum UserRoles {
  ADMIN("ADMIN"),
  USER("USER");

  private final String role;

  UserRoles(String role) {
    this.role = role;
  }

  public String roleName() {
    return role;
  }
}
