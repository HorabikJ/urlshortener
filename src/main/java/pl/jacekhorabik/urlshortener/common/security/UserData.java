package pl.jacekhorabik.urlshortener.common.security;

import lombok.Data;

@Data
public class UserData {
  private String userId;
  private String email;
  private String preferredUsername;
  private boolean authenticated;
}
