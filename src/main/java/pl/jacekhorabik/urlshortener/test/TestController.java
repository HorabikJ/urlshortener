package pl.jacekhorabik.urlshortener.test;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.jacekhorabik.urlshortener.security.aspects.AddUserDataToModel;

@Controller
public class TestController {

  @GetMapping("/admin")
  @AddUserDataToModel
  public ModelAndView getAdminView(ModelAndView modelAndView, Authentication auth) {
    modelAndView.setViewName("_admin");
    return modelAndView;
  }

  @GetMapping("/user")
  @AddUserDataToModel
  public ModelAndView getUserView(ModelAndView modelAndView, Authentication auth) {
    modelAndView.setViewName("_user");
    return modelAndView;
  }

}
