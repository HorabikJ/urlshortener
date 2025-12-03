package pl.jacekhorabik.urlshortener.pages.mainpage;

import static pl.jacekhorabik.urlshortener.pages.common.view.RedirectView.REDIRECT;
import static pl.jacekhorabik.urlshortener.pages.common.view.View.NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import pl.jacekhorabik.urlshortener.pages.common.entity.UrlEntity;

@Controller
@RequestMapping("/v1")
@RequiredArgsConstructor
class RedirectController {

  private final ShortenUrlService urlShorteningService;

  @GetMapping("/r/{hash}")
  ModelAndView redirect(final @PathVariable String hash) {
    return urlShorteningService
        .findUrlByHash(hash)
        .map(UrlEntity::getUrl)
        .map(
            url -> {
              RedirectView redirectView = new RedirectView(url);
              redirectView.setStatusCode(HttpStatus.FOUND);
              return new ModelAndView(redirectView);
            })
        .orElseGet(() -> new ModelAndView(REDIRECT.to(NOT_FOUND)));
  }
}
