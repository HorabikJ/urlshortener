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
        LinkDTO linkDTO = new LinkDTO();
        model.addAttribute("linkDTO", linkDTO);
        return "mainpage";
    }
    
    @PostMapping("/links")
    public String shortenLink(LinkDTO linkDTO) {
        return "mainpage";
    }

}
