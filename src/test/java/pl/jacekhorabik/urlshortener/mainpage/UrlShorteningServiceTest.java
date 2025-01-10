package pl.jacekhorabik.urlshortener.mainpage;


import io.seruco.encoding.base62.Base62;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("h2")
class UrlShorteningServiceTest {

  @Autowired private UrlRepository urlRepository;

  private UrlShorteningService urlShorteningService;

  @BeforeEach
  public void setUp() {
    urlShorteningService = new UrlShorteningService(Base62.createInstance(), urlRepository);
  }

}
