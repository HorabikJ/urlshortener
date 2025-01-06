package pl.jacekhorabik.urlshortener.mainpage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1")
public class MainPageController {
    
    @GetMapping("/")
    public String mainPage() {
        return "mainpage";
    }

}
