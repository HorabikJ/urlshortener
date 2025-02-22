package pl.jacekhorabik.urlshortener.shortenurl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.jacekhorabik.urlshortener.common.viewname.ViewName;
import pl.jacekhorabik.urlshortener.security.aspects.AddUserDataToModel;

@RequestMapping("/v1")
@Controller
@RequiredArgsConstructor
class MainPageController {

  private final UrlShorteningService urlShorteningService;

  @Value("${nodeport:8081}")
  private String nodePort;

  @Value("${app.host.ip:localhost}")
  private String hostIP;

  @GetMapping("/")
  @AddUserDataToModel
  ModelAndView mainPage(@NotNull final ModelAndView modelAndView, final Authentication auth) {
    final UrlDTO requestUrlDTO = new UrlDTO();
    modelAndView.setViewName(ViewName.MAIN_PAGE.viewName());
    modelAndView.addObject("requestUrlDTO", requestUrlDTO);
    return modelAndView;
  }

  @PostMapping("/")
  @AddUserDataToModel
  // todo add exception handler
  ModelAndView shortenUrl(
      final UrlDTO urlDTO, @NotNull final ModelAndView modelAndView, final Authentication auth)
      throws DecoderException {
    //    todo implement URL validation, url string has to be a valid url and can not be a domain
    // name of the app
    final String hash = urlShorteningService.shortenUrl(urlDTO).getHash();
    final UrlDTO responseUrlDTO =
        new UrlDTO(String.format("http://%s:%s/v1/r/%s", hostIP, nodePort, hash));
    modelAndView.addObject("responseUrlDTO", responseUrlDTO);
    modelAndView.addObject("requestUrlDTO", new UrlDTO());
    modelAndView.setViewName(ViewName.MAIN_PAGE.viewName());
    modelAndView.setStatus(HttpStatus.CREATED);
    return modelAndView;
  }
}
