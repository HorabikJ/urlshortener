package pl.jacekhorabik.urlshortener.mainpage;

import static org.assertj.core.api.Assertions.assertThat;

import io.seruco.encoding.base62.Base62;
import java.util.Optional;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UrlShorteningServiceTest {

  @Autowired private UrlRepository urlRepository;

  private UrlShorteningService urlShorteningService;

  @BeforeEach
  public void setUp() {
    urlShorteningService = new UrlShorteningService(Base62.createInstance(), urlRepository);
  }

  @Test
  void test() throws DecoderException {
    String hash = urlShorteningService.shortenUrl("https://www.google.pl/");
    urlRepository.flush();

    Optional<UrlEntity> urlEntityByHash = urlRepository.findUrlEntityByHash(hash);
    assertThat(urlEntityByHash.isPresent()).isTrue();
    UrlEntity urlEntity = urlEntityByHash.get();
    assertThat(urlEntity.getHash()).isEqualTo(hash);
    assertThat(urlEntity.getUrl()).isEqualTo("https://www.google.pl/");
  }
}
