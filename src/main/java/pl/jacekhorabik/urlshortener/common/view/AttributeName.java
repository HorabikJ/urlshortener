package pl.jacekhorabik.urlshortener.common.view;

public enum AttributeName {
  RESPONSE_URL_DTO("responseUrlDTO"),
  REQUEST_URL_DTO("requestUrlDTO"),
  USER_URLS_DTO("userUrlsDTO");

  private final String attributeName;

  AttributeName(String attributeName) {
    this.attributeName = attributeName;
  }

  @Override
  public String toString() {
    return attributeName;
  }
}
