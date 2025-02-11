package pl.jacekhorabik.urlshortener.test;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.jacekhorabik.urlshortener.security.aspects.AddAuthorizationToModelAndView;

@Controller
public class TestController {

  @GetMapping("/")
  @AddAuthorizationToModelAndView
  public ModelAndView getIndex(ModelAndView modelAndView, Authentication auth) {
    modelAndView.setViewName("index");
    return modelAndView;
  }

  @GetMapping("/nice")
  public ModelAndView getNice(ModelAndView modelAndView, Authentication auth) {
    modelAndView.setViewName("nice");
    return modelAndView;
  }
}
