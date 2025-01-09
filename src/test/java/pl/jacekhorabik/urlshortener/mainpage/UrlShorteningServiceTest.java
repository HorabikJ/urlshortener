package pl.jacekhorabik.urlshortener.mainpage;

import static org.assertj.core.api.Assertions.assertThat;

import io.seruco.encoding.base62.Base62;
import java.time.Instant;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// todo implement execution of @DataJpaTest against in-memory h2 database
public class UrlShorteningServiceTest {

  @Autowired private UrlRepository urlRepository;

  private UrlShorteningService urlShorteningService;

  @BeforeEach
  public void setUp() {
    urlShorteningService = new UrlShorteningService(Base62.createInstance(), urlRepository);
  }

  @Test
  // todo rename method
  void test() throws DecoderException {
    String hash = urlShorteningService.shortenUrl("https://www.google.pl/");
    urlRepository.flush();

    UrlEntity urlEntity = urlRepository.findUrlEntityByHash(hash).get();
    assertThat(urlEntity).isNotNull();
    assertThat(urlEntity.getHash()).isEqualTo(hash);
    assertThat(urlEntity.getUrl()).isEqualTo("https://www.google.pl/");
    assertThat(urlEntity.getCreatedAt()).isBefore(Instant.now());
    assertThat(urlEntity.getUpdatedAt()).isBefore(Instant.now());
  }
}
