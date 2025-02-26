package pl.jacekhorabik.urlshortener.keycloak;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.jacekhorabik.urlshortener.common.viewname.ViewName;
import pl.jacekhorabik.urlshortener.security.aspects.AddUserDataToModel;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
class KeycloakController {

  private final KeycloakService keycloakService;

  @GetMapping("/register")
  @AddUserDataToModel
  ModelAndView registerUserPage(ModelAndView modelAndView) {
    modelAndView.addObject("userDTO", new UserDTO());
    modelAndView.setViewName(ViewName.REGISTER.toString());
    return modelAndView;
  }

  @PostMapping("/register")
  @AddUserDataToModel
  ModelAndView createUser(ModelAndView modelAndView, UserDTO userDTO)
      throws KeycloakUserCreationException {
    keycloakService.registerUser(userDTO);
    modelAndView.addObject("userDTO", new UserDTO());
    modelAndView.setViewName(ViewName.REGISTER.toString());
    return modelAndView;
  }
}
