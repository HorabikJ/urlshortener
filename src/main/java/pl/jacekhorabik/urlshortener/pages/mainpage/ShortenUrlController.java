package pl.jacekhorabik.urlshortener.pages.mainpage;

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
import pl.jacekhorabik.urlshortener.common.dto.UserDataDTO;
import pl.jacekhorabik.urlshortener.common.view.AttributeName;
import pl.jacekhorabik.urlshortener.common.view.ViewName;
import pl.jacekhorabik.urlshortener.security.aspects.PopulateUserData;

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
  @PopulateUserData
  ModelAndView shortenUrl(
      final RequestUrlDTO urlDTO,
      final ModelAndView modelAndView,
      final UserDataDTO userData,
      final RedirectAttributes redirectAttributes)
      throws DecoderException, URISyntaxException {

    URI redirectUrl = new URI(urlDTO.url());
    URI appUrl = new URI(appExternalBaseUrl);
    if (redirectUrl.getHost().equals(appUrl.getHost())) {
      // todo implement URL validation, url string has to be a valid url and can not be a domain
      // name of the app
      System.out.println("Do not redirect.");
    }

    final String hash = urlShorteningService.shortenUrl(urlDTO, userData).getHash();

    redirectAttributes.addFlashAttribute(
        AttributeName.RESPONSE_URL_DTO.toString(),
        new ResponseUrlDTO(redirectUrlBuilder.buildRedirectUrl(hash).toString()));

    modelAndView.setViewName(ViewName.REDIRECT + "/v1");

    return modelAndView;
  }

  @PostMapping("/delete")
  @PopulateUserData
  ModelAndView deleteUrl(
      @RequestParam final String hash,
      final ModelAndView modelAndView,
      final UserDataDTO userData) {
    urlShorteningService.deleteUserUrlByHash(hash, userData);
    modelAndView.setViewName(ViewName.REDIRECT + "/v1");
    return modelAndView;
  }
}
