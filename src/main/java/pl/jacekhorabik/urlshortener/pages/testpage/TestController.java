package pl.jacekhorabik.urlshortener.pages.testpage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
class TestController {

  @GetMapping("/admin")
  ModelAndView getAdminView(ModelAndView modelAndView) {
    modelAndView.setViewName("_admin");
    return modelAndView;
  }

  @GetMapping("/user")
  ModelAndView getUserView(ModelAndView modelAndView) {
    modelAndView.setViewName("_user");
    return modelAndView;
  }
}
