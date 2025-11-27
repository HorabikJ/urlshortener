package pl.jacekhorabik.urlshortener.shortenurl;

record RequestUrlDTO(String url) {

  RequestUrlDTO() {
    this(null);
  }
}
