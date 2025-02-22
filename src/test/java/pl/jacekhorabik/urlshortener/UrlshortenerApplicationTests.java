package pl.jacekhorabik.urlshortener;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("h2")
class UrlshortenerApplicationTests {

  @Test
  void contextLoads() {}

  //    todo use testcontainers library for tests
  //    todo initialize test db with some data from script to have a good full db for testing

}
