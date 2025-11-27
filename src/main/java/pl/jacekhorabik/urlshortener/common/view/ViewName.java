package pl.jacekhorabik.urlshortener.common.view;

public enum ViewName {
  MAIN_PAGE("main-page"),
  NOT_FOUND("not-found"),
  REDIRECT("redirect:");

  private final String viewName;

  ViewName(String viewName) {
    this.viewName = viewName;
  }

  @Override
  public String toString() {
    return viewName;
  }
}
