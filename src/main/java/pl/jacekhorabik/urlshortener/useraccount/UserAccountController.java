package pl.jacekhorabik.urlshortener.useraccount;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.jacekhorabik.urlshortener.common.model.UserDataDTO;
import pl.jacekhorabik.urlshortener.common.view.AttributeName;
import pl.jacekhorabik.urlshortener.common.view.ViewName;
import pl.jacekhorabik.urlshortener.security.aspects.PopulateUserData;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/account")
@Slf4j
class UserAccountController {

  private final UserAccountService userAccountService;

  @GetMapping("/info")
  @PopulateUserData
  public ModelAndView accountInfo(final ModelAndView modelAndView, final UserDataDTO userData) {
    modelAndView.addObject(AttributeName.USER_DATA_DTO.toString(), userData);
    modelAndView.setViewName(ViewName.ACCOUNT_INFO.toString());
    return modelAndView;
  }

  @PostMapping("/delete")
  @PopulateUserData
  public ModelAndView deleteAccount(
      final ModelAndView modelAndView,
      final HttpServletRequest request,
      final HttpServletResponse response,
      final UserDataDTO userData,
      final Authentication authentication) {

    // Delete user from Keycloak
    userAccountService.deleteUserAccount(userData);
    // Logout the user and clear the session
    new SecurityContextLogoutHandler().logout(request, response, authentication);

    modelAndView.setViewName(ViewName.REDIRECT + "/v1");
    return modelAndView;
  }
}
