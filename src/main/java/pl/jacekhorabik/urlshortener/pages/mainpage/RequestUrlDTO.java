package pl.jacekhorabik.urlshortener.pages.mainpage;

record RequestUrlDTO(String url) {

  RequestUrlDTO() {
    this(null);
  }
}
