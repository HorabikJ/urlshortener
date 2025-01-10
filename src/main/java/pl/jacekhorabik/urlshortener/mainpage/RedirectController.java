package pl.jacekhorabik.urlshortener.mainpage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/v1")
@RequiredArgsConstructor
class RedirectController {

  private final UrlShorteningService urlShorteningService;

  @GetMapping("/r/{hash}")
  ModelAndView redirect(final @PathVariable String hash, final ModelAndView modelAndView) {
    return urlShorteningService
        .findUrlByHash(hash)
        .map(UrlEntity::getUrl)
        .map(
            url -> {
              modelAndView.setStatus(HttpStatus.FOUND);
              modelAndView.setViewName("redirect:" + url);
              return modelAndView;
            })
        .orElseGet(
            () -> {
              modelAndView.setViewName("not-found");
              return modelAndView;
            });
  }
}
