package pl.jacekhorabik.urlshortener.pages.useraccountpage;

import static pl.jacekhorabik.urlshortener.pages.common.view.RedirectView.REDIRECT;
import static pl.jacekhorabik.urlshortener.pages.common.view.View.ACCOUNT_INFO;
import static pl.jacekhorabik.urlshortener.pages.common.view.View.MAIN_PAGE;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.jacekhorabik.urlshortener.config.security.aspects.PopulateUserAuthentication;
import pl.jacekhorabik.urlshortener.pages.common.dto.UserAuthentication;
import pl.jacekhorabik.urlshortener.pages.common.view.AttributeName;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/account")
@Slf4j
class UserAccountPageController {

  private final UserAccountService userAccountService;

  @GetMapping("/info")
  @PopulateUserAuthentication
  public ModelAndView accountInfo(
      final ModelAndView modelAndView, final UserAuthentication userAuthentication) {
    final Map<String, Object> model = new HashMap<>();

    model.put(
        AttributeName.USER_ACCOUNT_DTO.toString(),
        userAccountService.fetchUserAccountInfo(userAuthentication));
    model.put(AttributeName.USER_AUTHENTICATION.toString(), userAuthentication);
    model.put(AttributeName.VIEW_NAME.toString(), ACCOUNT_INFO.toString());

    modelAndView.addAllObjects(model);
    modelAndView.setViewName(ACCOUNT_INFO.toString());
    return modelAndView;
  }

  @PostMapping("/delete")
  @PopulateUserAuthentication
  public ModelAndView deleteAccount(
      final ModelAndView modelAndView,
      final HttpServletRequest request,
      final HttpServletResponse response,
      final UserAuthentication userAuthentication,
      final Authentication authentication) {

    // Delete user from Keycloak
    userAccountService.deleteUserAccount(userAuthentication);
    // Logout the user and clear the session
    new SecurityContextLogoutHandler().logout(request, response, authentication);

    modelAndView.setViewName(REDIRECT.to(MAIN_PAGE));
    return modelAndView;
  }
}
