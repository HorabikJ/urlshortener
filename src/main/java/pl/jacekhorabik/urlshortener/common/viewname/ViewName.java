package pl.jacekhorabik.urlshortener.common.viewname;

public enum ViewName {
  MAIN_PAGE("main-page"),
  NOT_FOUND("not-found");

  private final String viewName;

  ViewName(String viewName) {
    this.viewName = viewName;
  }

  @Override
  public String toString() {
    return viewName;
  }
  
}
