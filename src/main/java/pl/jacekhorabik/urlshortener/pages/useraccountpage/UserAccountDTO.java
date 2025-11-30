package pl.jacekhorabik.urlshortener.pages.useraccountpage;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

record UserAccountDTO(String username, String email, long numberOfLinks, String createdAt) {

  private static final String DATE_TIME_FORMAT_STRING = "dd LLL YYYY HH:mm:ss";
  private static final DateTimeFormatter DATE_TIME_FORMAT =
      DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_STRING);

  UserAccountDTO(String username, String email, long numberOfLinks, Instant createdAt) {
    this(
        username,
        email,
        numberOfLinks,
        LocalDateTime.ofInstant(createdAt, ZoneId.systemDefault()).format(DATE_TIME_FORMAT));
  }
}
