package pl.jacekhorabik.urlshortener.pages.mainpage;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

record ResponseUrlDTO(String shortUrl, String originalUrl, String hash, String createdAt) {

  private static final String DATE_TIME_FORMAT_STRING = "dd LLL YYYY HH:mm:ss";
  private static final DateTimeFormatter DATE_TIME_FORMAT =
      DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_STRING);

  ResponseUrlDTO(String shortUrl) {
    this(shortUrl, "", "", "");
  }

  ResponseUrlDTO(String shortUrl, String originalUrl, String hash, Instant createdAt) {
    this(
        shortUrl,
        originalUrl,
        hash,
        LocalDateTime.ofInstant(createdAt, ZoneId.systemDefault()).format(DATE_TIME_FORMAT));
  }
}
