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
import pl.jacekhorabik.urlshortener.pages.common.dto.UserDataDTO;
import pl.jacekhorabik.urlshortener.pages.common.view.AttributeName;
import pl.jacekhorabik.urlshortener.security.aspects.PopulateUserData;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/account")
@Slf4j
class UserAccountPageController {

  private final UserAccountService userAccountService;

  @GetMapping("/info")
  @PopulateUserData
  public ModelAndView accountInfo(final ModelAndView modelAndView, final UserDataDTO userData) {
    final Map<String, Object> model = new HashMap<>();

    model.put(AttributeName.USER_DATA_DTO.toString(), userData);
    model.put(AttributeName.VIEW_NAME.toString(), ACCOUNT_INFO.toString());

    modelAndView.addAllObjects(model);
    modelAndView.setViewName(ACCOUNT_INFO.toString());
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

    modelAndView.setViewName(REDIRECT.to(MAIN_PAGE));
    return modelAndView;
  }
}
