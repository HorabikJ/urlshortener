package pl.jacekhorabik.urlshortener.mainpage;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UrlShorteningServiceIntegrationTest {

  @Autowired private UrlShorteningService urlShorteningService;

  @Test
  @Transactional
  void shouldSuccessfullySaveShortenedUrl() throws DecoderException {
    String hash = urlShorteningService.shortenUrl("https://www.google.pl/");

    String urlByHash = urlShorteningService.findUrlByHash(hash).get();

    assertThat(urlByHash).isEqualTo("https://www.google.pl/");
  }
}
