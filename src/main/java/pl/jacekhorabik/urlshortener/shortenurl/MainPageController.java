package pl.jacekhorabik.urlshortener.shortenurl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.jacekhorabik.urlshortener.common.model.UserData;
import pl.jacekhorabik.urlshortener.common.view.AttributeName;
import pl.jacekhorabik.urlshortener.common.view.ViewName;
import pl.jacekhorabik.urlshortener.security.aspects.PopulateUserData;

@Slf4j
@RequestMapping("/v1")
@Controller
@RequiredArgsConstructor
class MainPageController {

  private final UrlShorteningService urlShorteningService;

  @Value("${app.external-base-url}")
  private String appExternalBaseUrl;

  @GetMapping("/")
  @PopulateUserData
  ModelAndView mainPage(final ModelAndView modelAndView, final UserData userData) {
    final UrlDTO requestUrlDTO = new UrlDTO();
    modelAndView.setViewName(ViewName.MAIN_PAGE.toString());
    modelAndView.addObject(AttributeName.REQUEST_URL_DTO.toString(), requestUrlDTO);
    return modelAndView;
  }

  // todo add exception handler
  @PostMapping("/")
  @PopulateUserData
  ModelAndView shortenUrl(
      final UrlDTO urlDTO, final ModelAndView modelAndView, final UserData userData)
      throws DecoderException {
    //    todo implement URL validation, url string has to be a valid url and can not be a domain
    // name of the app
    final String hash = urlShorteningService.shortenUrl(urlDTO, userData).getHash();
    final UrlDTO responseUrlDTO = new UrlDTO(constructRedirectUrl(hash));
    final Map<String, Object> models = new HashMap<>();

    if (userData.isAuthenticated()) {
      final List<UserUrlDTO> userUrls =
          urlShorteningService.findUrlsByUserId(userData.getUserId()).stream()
              .map(entity -> new UserUrlDTO(entity.getUrl(), constructRedirectUrl(hash)))
              .toList();
      models.put(AttributeName.USER_URL_DTO.toString(), userUrls);
    }
    models.put(AttributeName.RESPONSE_URL_DTO.toString(), responseUrlDTO);
    models.put(AttributeName.REQUEST_URL_DTO.toString(), new UrlDTO());

    modelAndView.addAllObjects(models);
    modelAndView.setViewName(ViewName.MAIN_PAGE.toString());
    modelAndView.setStatus(HttpStatus.CREATED);

    return modelAndView;
  }

  private String constructRedirectUrl(String hash) {
    return appExternalBaseUrl + "/v1/r/" + hash;
  }
}
