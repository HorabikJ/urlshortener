package pl.jacekhorabik.urlshortener.mainpage;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
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
    final UrlEntity saved = urlShorteningService.shortenUrl(new UrlDTO("https://www.google.pl/"));
    final UrlEntity fetched = urlShorteningService.findUrlByHash(saved.getHash()).get();

    assertThat(fetched).isNotNull();
    assertThat(fetched.getUrl()).isEqualTo("https://www.google.pl/");
    assertThat(fetched.getHash()).isEqualTo("Gai1aEc");
    assertThat(fetched.getCreatedAt()).isNotNull();
    assertThat(fetched.getUpdatedAt()).isNotNull();
    assertThat(fetched.getCreatedAt()).isEqualTo(fetched.getUpdatedAt());
  }

  @Test
  @Transactional
  void shouldGenerateTwoDifferentHashesForTheSameUrlSavedTwice() throws DecoderException {
    // primary hash for https://www.google.pl is Gai1aEc
    final UrlEntity savedFirst =
        urlShorteningService.shortenUrl(new UrlDTO("https://www.google.pl/"));
    final UrlEntity savedSecond =
        urlShorteningService.shortenUrl(new UrlDTO("https://www.google.pl/"));

    final UrlEntity urlEntityFirst = urlShorteningService.findUrlByHash(savedFirst.getHash()).get();
    final UrlEntity urlEntitySecond =
        urlShorteningService.findUrlByHash(savedSecond.getHash()).get();

    assertThat(urlEntityFirst.getHash()).isEqualTo("Gai1aEc");
    assertThat(urlEntityFirst.getUrl()).isEqualTo("https://www.google.pl/");

    assertThat(urlEntitySecond.getHash()).isNotNull();
    assertThat(urlEntitySecond.getHash()).isNotEqualTo("Gai1aEc");
    assertThat(urlEntitySecond.getUrl()).isEqualTo("https://www.google.pl/");
  }

  @Test
  @Sql(
      statements =
          "INSERT INTO urls (hash, url, created_at, updated_at) "
              + "VALUES ('2sV5hKo', 'https://example.com', '2025-01-01 01:01:01.000', '2025-01-01 01:01:01.000')",
      executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(
      statements = "DELETE FROM urls WHERE hash = '2sV5hKo'",
      executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void shouldFetchUrlThatIsAlreadyInDB() {
    final UrlEntity urlByHash = urlShorteningService.findUrlByHash("2sV5hKo").get();

    assertThat(urlByHash.getUrl()).isEqualTo("https://example.com");
    assertThat(urlByHash.getHash()).isEqualTo("2sV5hKo");
    assertThat(urlByHash.getCreatedAt()).isEqualTo("2025-01-01T01:01:01Z");
    assertThat(urlByHash.getUpdatedAt()).isEqualTo("2025-01-01T01:01:01Z");
  }
}
