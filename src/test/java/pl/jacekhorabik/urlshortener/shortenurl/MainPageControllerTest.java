package pl.jacekhorabik.urlshortener.shortenurl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.jacekhorabik.urlshortener.common.view.ViewName;
import pl.jacekhorabik.urlshortener.pages.mainpage.MainPageController;
import pl.jacekhorabik.urlshortener.pages.mainpage.ResponseUrlDTO;
import pl.jacekhorabik.urlshortener.pages.mainpage.ShortenUrlService;
import pl.jacekhorabik.urlshortener.pages.mainpage.UrlEntity;

@WebMvcTest(controllers = MainPageController.class)
@ActiveProfiles({"local"})
class MainPageControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private ShortenUrlService urlShorteningService;

  @Test
  void shouldReturnMainPage() throws Exception {
    this.mockMvc
        .perform(get("/v1/"))
        .andExpect(status().isOk())
        .andExpect(view().name(ViewName.MAIN_PAGE.toString()))
        .andExpect(model().attributeExists("requestUrlDTO"));
  }

  @Test
  void shouldReturnMainPageWithShortenedUrl() throws Exception {
    final String url = "http://example.com";

    when(urlShorteningService.shortenUrl(new ResponseUrlDTO(url)))
        .thenReturn(new UrlEntity("hash123", url));

    this.mockMvc
        .perform(post("/v1/").param("url", url))
        .andExpect(status().isCreated())
        .andExpect(view().name(ViewName.MAIN_PAGE.toString()))
        .andExpect(model().attribute("requestUrlDTO", new ResponseUrlDTO()))
        .andExpect(
            model()
                .attribute(
                    "responseUrlDTO", new ResponseUrlDTO("http://localhost:8080/v1/r/hash123")));
  }
}
