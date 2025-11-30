package pl.jacekhorabik.urlshortener.pages.common.dto;

import lombok.Data;

@Data
public class UserAuthentication {
  private String userId;
  private String preferredUsername;
  private boolean authenticated;
}
