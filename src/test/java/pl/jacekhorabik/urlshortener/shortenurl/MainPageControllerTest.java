package pl.jacekhorabik.urlshortener.shortenurl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.jacekhorabik.urlshortener.common.viewname.ViewName;

@WebMvcTest(controllers = MainPageController.class)
class MainPageControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private UrlShorteningService urlShorteningService;

  @Test
  void shouldReturnMainPage() throws Exception {
    this.mockMvc
        .perform(get("/v1/"))
        .andExpect(status().isOk())
        .andExpect(view().name(ViewName.MAIN_PAGE.viewName()))
        .andExpect(model().attributeExists("requestUrlDTO"));
  }

  @Test
  void shouldReturnMainPageWithShortenedUrl() throws Exception {
    final String url = "http://example.com";

    when(urlShorteningService.shortenUrl(new UrlDTO(url)))
        .thenReturn(new UrlEntity("hash123", url));

    this.mockMvc
        .perform(post("/v1/").param("url", url))
        .andExpect(status().isCreated())
        .andExpect(view().name(ViewName.MAIN_PAGE.viewName()))
        .andExpect(model().attribute("requestUrlDTO", new UrlDTO()))
        .andExpect(
            model().attribute("responseUrlDTO", new UrlDTO("http://localhost:8080/v1/r/hash123")));
  }
}
