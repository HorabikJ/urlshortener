package pl.jacekhorabik.urlshortener.pages.mainpage;

import static pl.jacekhorabik.urlshortener.pages.common.view.View.MAIN_PAGE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.jacekhorabik.urlshortener.pages.common.dto.UserAuthentication;
import pl.jacekhorabik.urlshortener.pages.common.view.AttributeName;
import pl.jacekhorabik.urlshortener.pages.common.view.View;
import pl.jacekhorabik.urlshortener.security.aspects.PopulateUserAuthentication;

@Slf4j
@RequestMapping("/v1")
@Controller
@RequiredArgsConstructor
class MainPageController {

  private final ShortenUrlService urlShorteningService;
  private final RedirectUrlBuilder redirectUrlBuilder;

  @GetMapping
  @PopulateUserAuthentication
  ModelAndView mainPage(
      final ModelAndView modelAndView, final UserAuthentication userAuthentication) {
    final HashMap<String, Object> models = new HashMap<>();

    addUserUrlsToModel(userAuthentication, models);
    models.put(AttributeName.REQUEST_URL_DTO.toString(), new RequestUrlDTO());
    models.put(AttributeName.USER_AUTHENTICATION.toString(), userAuthentication);
    models.put(AttributeName.VIEW_NAME.toString(), MAIN_PAGE.toString());

    modelAndView.addAllObjects(models);
    modelAndView.setViewName(View.MAIN_PAGE.toString());

    return modelAndView;
  }

  private void addUserUrlsToModel(
      final UserAuthentication userData, final Map<String, Object> models) {
    if (userData.isAuthenticated()) {
      final List<ResponseUrlDTO> userUrls =
          urlShorteningService.findUrlsByUserId(userData.getUserId()).stream()
              .map(
                  entity ->
                      new ResponseUrlDTO(
                          redirectUrlBuilder.buildRedirectUrl(entity.getHash()).toString(),
                          entity.getUrl(),
                          entity.getHash()))
              .toList();
      models.put(AttributeName.USER_URLS_DTO.toString(), userUrls);
    }
  }

}
