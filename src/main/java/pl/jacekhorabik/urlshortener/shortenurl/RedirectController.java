package pl.jacekhorabik.urlshortener.shortenurl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.jacekhorabik.urlshortener.viewname.ViewName;

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
              modelAndView.setViewName(ViewName.NOT_FOUND.viewName());
              modelAndView.setStatus(HttpStatus.NOT_FOUND);
              return modelAndView;
            });
  }
}
