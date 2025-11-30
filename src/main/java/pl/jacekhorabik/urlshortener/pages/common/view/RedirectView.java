package pl.jacekhorabik.urlshortener.pages.common.view;

public enum RedirectView {
  REDIRECT("redirect:");

  private final String redirectAction;

  RedirectView(final String redirectAction) {
    this.redirectAction = redirectAction;
  }

  public String to(final View view) {
    return redirectAction + view.getViewPath();
  }
}
