package pl.jacekhorabik.urlshortener.pages.common.dto;

import lombok.Data;

@Data
public class UserDataDTO {
  private String userId;
  private String email;
  private String preferredUsername;
  private boolean authenticated;
}
