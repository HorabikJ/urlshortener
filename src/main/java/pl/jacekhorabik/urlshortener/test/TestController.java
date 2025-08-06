package pl.jacekhorabik.urlshortener.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {

  @GetMapping("/admin")
  public ModelAndView getAdminView(ModelAndView modelAndView) {
    modelAndView.setViewName("_admin");
    return modelAndView;
  }

  @GetMapping("/user")
  public ModelAndView getUserView(ModelAndView modelAndView) {
    modelAndView.setViewName("_user");
    return modelAndView;
  }
}
