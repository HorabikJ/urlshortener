package pl.jacekhorabik.urlshortener.pages.common.view;

import lombok.Getter;

public enum View {
  MAIN_PAGE("main-page", "/v1"),
  ACCOUNT_INFO("account-info", "/v1/account/info"),
  NOT_FOUND("not-found", "/v1/not-found");

  private final String viewName;
  @Getter private final String viewPath;

  View(final String viewName, final String viewPath) {
    this.viewName = viewName;
    this.viewPath = viewPath;
  }

  @Override
  public String toString() {
    return viewName;
  }
}
