package pl.jacekhorabik.urlshortener.pages.common.view;

public enum AttributeName {
  RESPONSE_URL_DTO("responseUrlDTO"),
  REQUEST_URL_DTO("requestUrlDTO"),
  USER_URLS_PAGES_DTO("userUrlsPagesDTO"),
  USER_ACCOUNT_DTO("userAccountDTO"),
  PAGE_NUMBERS("pageNumbers"),
  USER_AUTHENTICATION("userAuthentication"),
  VIEW_NAME("viewName");

  private final String attributeName;

  AttributeName(String attributeName) {
    this.attributeName = attributeName;
  }

  @Override
  public String toString() {
    return attributeName;
  }
}
