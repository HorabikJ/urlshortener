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
class UrlShorteningServiceIntegrationTest {

  @Autowired private UrlShorteningService urlShorteningService;

  @Test
  @Transactional
  void shouldSuccessfullySaveAndFindShortenedUrl() throws DecoderException {
    // primary hash for https://www.google.pl is Gai1aEc
    UrlEntity saved = urlShorteningService.shortenUrl(new UrlDTO("https://www.google.pl/"));
    UrlEntity fetched = urlShorteningService.findUrlByHash(saved.getHash()).get();

    assertThat(fetched).isNotNull();
    assertThat(fetched.getUrl()).isEqualTo("https://www.google.pl/");
    assertThat(fetched.getHash()).isEqualTo("Gai1aEc");
    assertThat(fetched.getCreatedAt()).isNotNull();
    assertThat(fetched.getUpdatedAt()).isNotNull();
    assertThat(fetched.getCreatedAt()).isEqualTo(fetched.getUpdatedAt());
  }

  @Test
  @Transactional
  void shouldGenerateTwoDifferentHashesForTheSameUrlSavedTwice()
      throws DecoderException, InterruptedException {
    // primary hash for https://www.google.pl is Gai1aEc
    UrlEntity savedFirst = urlShorteningService.shortenUrl(new UrlDTO("https://www.google.pl/"));
    UrlEntity savedSecond = urlShorteningService.shortenUrl(new UrlDTO("https://www.google.pl/"));

    UrlEntity urlEntityFirst = urlShorteningService.findUrlByHash(savedFirst.getHash()).get();
    UrlEntity urlEntitySecond = urlShorteningService.findUrlByHash(savedSecond.getHash()).get();

    assertThat(urlEntityFirst.getHash()).isEqualTo("Gai1aEc");
    assertThat(urlEntitySecond.getHash()).isNotNull();
    assertThat(urlEntitySecond.getHash()).isNotEqualTo("Gai1aEc");
  }
}
