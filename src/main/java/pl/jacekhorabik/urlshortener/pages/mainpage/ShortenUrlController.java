package pl.jacekhorabik.urlshortener.pages.mainpage;

import static pl.jacekhorabik.urlshortener.pages.common.view.RedirectView.REDIRECT;
import static pl.jacekhorabik.urlshortener.pages.common.view.View.MAIN_PAGE;

import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.jacekhorabik.urlshortener.pages.common.dto.UserAuthentication;
import pl.jacekhorabik.urlshortener.pages.common.view.AttributeName;
import pl.jacekhorabik.urlshortener.security.aspects.PopulateUserAuthentication;

@Slf4j
@RequestMapping("/v1/url")
@Controller
@RequiredArgsConstructor
class ShortenUrlController {

  private final ShortenUrlService urlShorteningService;
  private final RedirectUrlBuilder redirectUrlBuilder;

  @Value("${app.external-base-url}")
  private String appExternalBaseUrl;

  // todo add exception handler
  @PostMapping("/create")
  @PopulateUserAuthentication
  ModelAndView shortenUrl(
      final RequestUrlDTO urlDTO,
      final ModelAndView modelAndView,
      final UserAuthentication userAuthentication,
      final RedirectAttributes redirectAttributes)
      throws DecoderException, URISyntaxException {

    URI redirectUrl = new URI(urlDTO.url());
    URI appUrl = new URI(appExternalBaseUrl);
    if (redirectUrl.getHost().equals(appUrl.getHost())) {
      // todo implement URL validation, url string has to be a valid url and can not be a domain
      // name of the app
      System.out.println("Do not redirect.");
    }

    final String hash = urlShorteningService.shortenUrl(urlDTO, userAuthentication).getHash();

    redirectAttributes.addFlashAttribute(
        AttributeName.RESPONSE_URL_DTO.toString(),
        new ResponseUrlDTO(redirectUrlBuilder.buildRedirectUrl(hash).toString()));

    modelAndView.setViewName(REDIRECT.to(MAIN_PAGE));

    return modelAndView;
  }

  @PostMapping("/delete")
  @PopulateUserAuthentication
  ModelAndView deleteUrl(
      @RequestParam final String hash,
      final ModelAndView modelAndView,
      final UserAuthentication userData) {
    urlShorteningService.deleteUserUrlByHash(hash, userData);
    modelAndView.setViewName(REDIRECT.to(MAIN_PAGE));
    return modelAndView;
  }
}
