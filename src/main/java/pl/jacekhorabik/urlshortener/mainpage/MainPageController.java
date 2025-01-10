package pl.jacekhorabik.urlshortener.mainpage;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/v1")
@Controller
@RequiredArgsConstructor
class MainPageController {

  private final UrlShorteningService urlShorteningService;

  @Value("${nodeport:8080}")
  private String nodePort;

  @GetMapping("/")
  String mainPage(final Model model) {
    final UrlDTO requestUrlDTO = new UrlDTO();
    model.addAttribute("requestUrlDTO", requestUrlDTO);
    return "mainpage";
  }

  @PostMapping("/")
  // todo add exception handler
  String shortenUrl(final UrlDTO urlDTO, final Model model) throws DecoderException {
    final String hash = urlShorteningService.shortenUrl(urlDTO).getHash();
    final UrlDTO responseUrlDTO = new UrlDTO(String.format("localhost:%s/v1/r/%s", nodePort, hash));
    model.addAttribute("responseUrlDTO", responseUrlDTO);
    model.addAttribute("requestUrlDTO", new UrlDTO());
    return "mainpage";
  }
}
