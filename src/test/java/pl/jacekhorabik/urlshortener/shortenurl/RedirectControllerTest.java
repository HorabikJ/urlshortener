package pl.jacekhorabik.urlshortener.shortenurl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.jacekhorabik.urlshortener.pages.common.view.View;
import pl.jacekhorabik.urlshortener.pages.mainpage.RedirectController;
import pl.jacekhorabik.urlshortener.pages.mainpage.ShortenUrlService;
import pl.jacekhorabik.urlshortener.pages.mainpage.UrlEntity;

@WebMvcTest(controllers = RedirectController.class)
@ActiveProfiles({"local"})
class RedirectControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private ShortenUrlService urlShorteningService;

  @Test
  void shouldRedirectToCorrectShortenedUrl() throws Exception {
    final String hash = "hash234";
    final String url = "http://example.com";

    when(urlShorteningService.findUrlByHash(hash))
        .thenReturn(Optional.of(new UrlEntity(hash, url)));

    mockMvc
        .perform(get("/v1/r/" + hash))
        .andExpect(status().is(HttpStatus.FOUND.value()))
        .andExpect(view().name("redirect:" + url));
  }

  @Test
  void shouldRedirectToCustomNotFoundPage() throws Exception {
    final String hash = "hash";
    when(urlShorteningService.findUrlByHash(hash)).thenReturn(Optional.empty());

    mockMvc
        .perform(get("/v1/r/" + hash))
        .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
        .andExpect(view().name(View.NOT_FOUND.toString()));
  }
}
