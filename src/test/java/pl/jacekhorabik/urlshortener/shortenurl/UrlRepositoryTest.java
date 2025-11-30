package pl.jacekhorabik.urlshortener.shortenurl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pl.jacekhorabik.urlshortener.pages.mainpage.UrlEntity;
import pl.jacekhorabik.urlshortener.pages.mainpage.UrlRepository;

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
    assertThat(urlEntity.getCreatedAt()).isNotNull();
    assertThat(urlEntity.getCreatedAt()).isNotNull();
    assertThat(urlEntity.getCreatedAt()).isEqualTo(urlEntity.getUpdatedAt());
  }
}
