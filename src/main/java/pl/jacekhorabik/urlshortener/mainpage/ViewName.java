package pl.jacekhorabik.urlshortener.mainpage;

enum ViewName {
  MAIN_PAGE("main-page"),
  NOT_FOUND("not-found");

  private final String viewName;

  ViewName(String viewName) {
    this.viewName = viewName;
  }

  String viewname() {
    return viewName;
  }
}
