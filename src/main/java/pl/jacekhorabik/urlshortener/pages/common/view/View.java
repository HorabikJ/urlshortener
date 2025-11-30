package pl.jacekhorabik.urlshortener.pages.common.view;

import lombok.Getter;

public enum View {
  MAIN_PAGE("main-page", "/v1"),
  ACCOUNT_INFO("account-info", "/v1/account/info"),
  NOT_FOUND("not-found", "/v1/not-found");

  private final String viewName;
  @Getter private final String viewUrl;

  View(final String viewName, final String viewURL) {
    this.viewName = viewName;
    this.viewUrl = viewURL;
  }

  @Override
  public String toString() {
    return viewName;
  }
}
