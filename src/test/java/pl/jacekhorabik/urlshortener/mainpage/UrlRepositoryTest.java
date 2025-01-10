package pl.jacekhorabik.urlshortener.mainpage;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("h2")
class UrlRepositoryTest {

  @Autowired private UrlRepository urlRepository;

  @Test
  void shouldFindUrlEntityByHash() {
    urlRepository.saveAndFlush(new UrlEntity("hash", "https://example.com"));

    UrlEntity urlEntity = urlRepository.findUrlEntityByHash("hash").get();

    assertThat(urlEntity).isNotNull();
    assertThat(urlEntity.getHash()).isEqualTo("hash");
    assertThat(urlEntity.getUrl()).isEqualTo("https://example.com");
    assertThat(urlEntity.getCreatedAt()).isBefore(Instant.now());
    assertThat(urlEntity.getUpdatedAt()).isBefore(Instant.now());
  }

  @Test
  @Disabled
  // todo investigate why createdAt is null
  void shouldUpdateUrlEntityUpdatedAtColumn() throws InterruptedException {
    urlRepository.saveAndFlush(new UrlEntity("hash", "https://example.com"));
    UrlEntity saved = urlRepository.findUrlEntityByHash("hash").get();

    TimeUnit.SECONDS.sleep(1);

    urlRepository.saveAndFlush(new UrlEntity("hash", "https://example.com"));
    UrlEntity updated = urlRepository.findUrlEntityByHash("hash").get();

    assertThat(saved.getCreatedAt()).isEqualTo(updated.getCreatedAt());
    assertThat(saved.getUpdatedAt()).isBefore(updated.getUpdatedAt());
  }
  
}
