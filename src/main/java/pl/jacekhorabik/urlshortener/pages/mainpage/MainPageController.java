package pl.jacekhorabik.urlshortener.pages.mainpage;

import static pl.jacekhorabik.urlshortener.pages.common.view.View.MAIN_PAGE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  private static final int DEFAULT_PAGE_SIZE = 5;
  private static final int FIRST_PAGE_INDEX = 0;

  private final ShortenUrlService urlShorteningService;
  private final RedirectUrlBuilder redirectUrlBuilder;

  @GetMapping
  @PopulateUserAuthentication
  ModelAndView mainPage(
      final ModelAndView modelAndView,
      final UserAuthentication userAuthentication,
      final @RequestParam Optional<Integer> pageIndex,
      final @RequestParam Optional<Integer> pageSize) {
    final HashMap<String, Object> models = new HashMap<>();

    addUserUrlsToModel(userAuthentication, models, pageIndex, pageSize);
    models.put(AttributeName.REQUEST_URL_DTO.toString(), new RequestUrlDTO());
    models.put(AttributeName.USER_AUTHENTICATION.toString(), userAuthentication);
    models.put(AttributeName.VIEW_NAME.toString(), MAIN_PAGE.toString());

    modelAndView.addAllObjects(models);
    modelAndView.setViewName(View.MAIN_PAGE.toString());

    return modelAndView;
  }

  private void addUserUrlsToModel(
      final UserAuthentication userData,
      final Map<String, Object> models,
      final Optional<Integer> pageIndex,
      final Optional<Integer> pageSize) {
    if (userData.isAuthenticated()) {
      final PageRequest pageRequest =
          PageRequest.of(
              pageIndex.orElse(FIRST_PAGE_INDEX),
              pageSize.orElse(DEFAULT_PAGE_SIZE),
              Sort.by(Sort.Direction.DESC, "createdAt"));

      final Page<ResponseUrlDTO> userUrls =
          urlShorteningService
              .findUrlsByUserId(userData.getUserId(), pageRequest)
              .map(
                  entity ->
                      new ResponseUrlDTO(
                          redirectUrlBuilder.buildRedirectUrl(entity.getHash()).toString(),
                          entity.getUrl(),
                          entity.getHash(),
                          entity.getCreatedAt()));

      final int totalPages = userUrls.getTotalPages();
      if (totalPages > 0) {
        final List<Integer> pageIndexes =
            IntStream.range(FIRST_PAGE_INDEX, totalPages).boxed().toList();
        models.put(AttributeName.PAGE_INDEXES.toString(), pageIndexes);
      }

      models.put(AttributeName.USER_URLS_PAGES_DTO.toString(), userUrls);
    }
  }
}
