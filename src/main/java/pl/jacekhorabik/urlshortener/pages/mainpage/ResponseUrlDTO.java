package pl.jacekhorabik.urlshortener.pages.mainpage;

record ResponseUrlDTO(String shortUrl, String originalUrl, String hash) {

  ResponseUrlDTO(String shortUrl) {
    this(shortUrl, null, null);
  }
  
}
