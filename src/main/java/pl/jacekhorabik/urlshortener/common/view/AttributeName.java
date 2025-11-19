package pl.jacekhorabik.urlshortener.common.view;

public enum AttributeName {
  RESPONSE_URL_DTO("responseUrlDTO"),
  REQUEST_URL_DTO("requestUrlDTO"),
  USER_URL_DTO("userUrlDTO");

  private final String attributeName;

  AttributeName(String attributeName) {
    this.attributeName = attributeName;
  }

  @Override
  public String toString() {
    return attributeName;
  }
}
