package pl.jacekhorabik.urlshortener.common.model;

import lombok.Data;

@Data
public class UserData {
  private String userId;
  private String email;
  private String preferredUsername;
  private boolean authenticated;
}
