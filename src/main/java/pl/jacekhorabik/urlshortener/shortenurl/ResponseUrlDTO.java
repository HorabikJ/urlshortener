package pl.jacekhorabik.urlshortener.shortenurl;

record ResponseUrlDTO(String shortUrl, String originalUrl, String hash) {

  ResponseUrlDTO() {
    this(null, null, null);
  }

  ResponseUrlDTO(String shortUrl) {
    this(shortUrl, null, null);
  }
}
