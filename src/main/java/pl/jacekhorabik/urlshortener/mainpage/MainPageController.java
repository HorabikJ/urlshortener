package pl.jacekhorabik.urlshortener.mainpage;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1")
class MainPageController {
    
    @GetMapping("/")
    public String mainPage(Model model) {
        UrlDTO urlDTO = new UrlDTO();
        model.addAttribute("urlDTO", urlDTO);
        return "mainpage";
    }
    
    @PostMapping("/urls")
    public String shortenUrl(UrlDTO urlDTO) {
        return "mainpage";
    }

}
