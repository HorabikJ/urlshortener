package pl.jacekhorabik.urlshortener.pages.mainpage;

import static pl.jacekhorabik.urlshortener.pages.common.view.RedirectView.REDIRECT;
import static pl.jacekhorabik.urlshortener.pages.common.view.View.MAIN_PAGE;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.jacekhorabik.urlshortener.config.security.aspects.PopulateUserAuthentication;
import pl.jacekhorabik.urlshortener.pages.common.dto.UserAuthentication;
import pl.jacekhorabik.urlshortener.pages.common.view.AttributeName;

@RequestMapping("/v1/url")
@Controller
@RequiredArgsConstructor
class ShortenUrlController {

  private final ShortenUrlService urlShorteningService;
  private final RedirectUrlBuilder redirectUrlBuilder;
  private final ShortenUrlValidator urlValidator;

  // todo add exception handler
  @PostMapping("/create")
  @PopulateUserAuthentication
  ModelAndView shortenUrl(
      final RequestUrlDTO urlDTO,
      final ModelAndView modelAndView,
      final UserAuthentication userAuthentication,
      final RedirectAttributes redirectAttributes)
      throws DecoderException {

    if (!urlValidator.isValidUrl(urlDTO.url())) {
      redirectAttributes.addFlashAttribute(AttributeName.INVALID_URL.toString(), StringUtils.EMPTY);
    } else {
      final String hash =
          urlShorteningService.shortenUrl(urlDTO.url(), userAuthentication).getHash();
      final String redirectURL = redirectUrlBuilder.buildRedirectUrl(hash);
      redirectAttributes.addFlashAttribute(
          AttributeName.RESPONSE_URL_DTO.toString(), new ResponseUrlDTO(redirectURL));
    }
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
