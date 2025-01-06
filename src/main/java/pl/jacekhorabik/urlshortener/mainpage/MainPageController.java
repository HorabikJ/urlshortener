package pl.jacekhorabik.urlshortener.mainpage;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.DecoderException;
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

    @GetMapping("/")
    public String mainPage(Model model) {
        UrlDTO urlDTO = new UrlDTO();
        model.addAttribute("urlDTO", urlDTO);
        return "mainpage";
    }

    @PostMapping("/urls")
    public String shortenUrl(UrlDTO urlDTO, Model model) throws DecoderException {
        String hash = urlShorteningService.shortenUrl(urlDTO.url());
        UrlDTO responseUrlDTO = new UrlDTO("localhost:8080/s/" + hash);
        model.addAttribute("responseUrlDTO", responseUrlDTO);
        return "mainpage";
    }

}
