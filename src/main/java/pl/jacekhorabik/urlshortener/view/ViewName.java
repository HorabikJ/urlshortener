package pl.jacekhorabik.urlshortener.view;

public enum ViewName {
  MAIN_PAGE("main-page"),
  NOT_FOUND("not-found");

  private final String viewName;

  ViewName(String viewName) {
    this.viewName = viewName;
  }

  public String viewName() {
    return viewName;
  }
}
