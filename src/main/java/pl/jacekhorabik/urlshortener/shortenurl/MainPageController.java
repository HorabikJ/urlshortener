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
import org.springframework.web.bind.annotation.RequestParam;
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
    final HashMap<String, Object> models = new HashMap<>();

    addUserUrlsToModel(userData, models);
    models.put(AttributeName.REQUEST_URL_DTO.toString(), new RequestUrlDTO());

    modelAndView.addAllObjects(models);
    modelAndView.setViewName(ViewName.MAIN_PAGE.toString());

    return modelAndView;
  }

  // todo add exception handler
  @PostMapping("/")
  @PopulateUserData
  ModelAndView shortenUrl(
      final RequestUrlDTO urlDTO, final ModelAndView modelAndView, final UserData userData)
      throws DecoderException {
    //    todo implement URL validation, url string has to be a valid url and can not be a domain
    // name of the app
    final Map<String, Object> models = new HashMap<>();
    final String hash = urlShorteningService.shortenUrl(urlDTO, userData).getHash();

    addUserUrlsToModel(userData, models);
    models.put(
        AttributeName.RESPONSE_URL_DTO.toString(), new ResponseUrlDTO(constructRedirectUrl(hash)));
    models.put(AttributeName.REQUEST_URL_DTO.toString(), new RequestUrlDTO());

    modelAndView.addAllObjects(models);
    modelAndView.setViewName(ViewName.MAIN_PAGE.toString());
    modelAndView.setStatus(HttpStatus.CREATED);

    return modelAndView;
  }

  @PostMapping("/url/delete")
  @PopulateUserData
  ModelAndView deleteUrl(
      @RequestParam final String hash, final ModelAndView modelAndView, final UserData userData) {
    final Map<String, Object> models = new HashMap<>();

    urlShorteningService.deleteUserUrlByHash(hash, userData);

    addUserUrlsToModel(userData, models);
    models.put(AttributeName.REQUEST_URL_DTO.toString(), new RequestUrlDTO());

    modelAndView.addAllObjects(models);
    modelAndView.setViewName(ViewName.MAIN_PAGE.toString());

    return modelAndView;
  }

  private void addUserUrlsToModel(final UserData userData, final Map<String, Object> models) {
    if (userData.isAuthenticated()) {
      final List<ResponseUrlDTO> userUrls =
          urlShorteningService.findUrlsByUserId(userData.getUserId()).stream()
              .map(
                  entity ->
                      new ResponseUrlDTO(
                          constructRedirectUrl(entity.getHash()),
                          entity.getUrl(),
                          entity.getHash()))
              .toList();
      models.put(AttributeName.USER_URLS_DTO.toString(), userUrls);
    }
  }

  private String constructRedirectUrl(String hash) {
    return appExternalBaseUrl + "/v1/r/" + hash;
  }
}
