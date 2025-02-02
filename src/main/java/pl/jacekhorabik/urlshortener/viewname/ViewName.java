package pl.jacekhorabik.urlshortener.viewname;

public enum ViewName {
  MAIN_PAGE("main-page"),
  NOT_FOUND("not-found"),
  REGISTER("register"),
  LOGIN("login");

  private final String viewName;

  ViewName(String viewName) {
    this.viewName = viewName;
  }

  public String viewName() {
    return viewName;
  }
}
