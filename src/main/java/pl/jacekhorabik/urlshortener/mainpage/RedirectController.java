package pl.jacekhorabik.urlshortener.mainpage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
class RedirectController {

  private final UrlShorteningService urlShorteningService;

  @GetMapping("/r/{hash}")
  ResponseEntity<?> redirect(@PathVariable String hash) {
    // todo: implement handling the not found case, some custom not found page?
    return urlShorteningService
        .findUrlByHash(hash)
        .map(url -> ResponseEntity.status(HttpStatus.FOUND.value()).header("Location", url).build())
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND.value()).build());
  }
}
