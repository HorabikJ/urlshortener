package pl.jacekhorabik.urlshortener.userregisterlogin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.jacekhorabik.urlshortener.common.viewname.ViewName;

@Controller
@RequestMapping("/v1")
@RequiredArgsConstructor
class UserRegisterLoginController {

  private final UserRegisterLoginService userRegisterLoginService;

  @GetMapping("/register")
  ModelAndView register(ModelAndView modelAndView) {
    modelAndView.setViewName(ViewName.REGISTER.viewName());
    modelAndView.addObject("userCredentialsDTO", new UserCredentialsDTO());
    return modelAndView;
  }

  @PostMapping("/register")
  ModelAndView registerUser(UserCredentialsDTO userCredentialsDTO, ModelAndView modelAndView) {
    userRegisterLoginService.registerUser(userCredentialsDTO);
    modelAndView.setViewName(ViewName.LOGIN.viewName());
    modelAndView.setStatus(HttpStatus.CREATED);
    return modelAndView;
  }
}
