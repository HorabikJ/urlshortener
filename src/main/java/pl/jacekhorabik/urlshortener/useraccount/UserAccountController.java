package pl.jacekhorabik.urlshortener.useraccount;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.jacekhorabik.urlshortener.common.model.UserDataDTO;
import pl.jacekhorabik.urlshortener.common.view.AttributeName;
import pl.jacekhorabik.urlshortener.common.view.ViewName;
import pl.jacekhorabik.urlshortener.security.aspects.PopulateUserData;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/account")
class UserAccountController {

  private final UserAccountService userAccountService;

  @GetMapping("/info")
  @PopulateUserData
  public ModelAndView accountInfo(final ModelAndView modelAndView, UserDataDTO userData) {
    modelAndView.addObject(AttributeName.USER_DATA_DTO.toString(), userData);
    modelAndView.setViewName(ViewName.ACCOUNT_INFO.toString());
    return modelAndView;
  }

  @PostMapping("/delete")
  public String deleteAccount(
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes redirectAttributes) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    try {
      // Delete user from Keycloak
      userAccountService.deleteCurrentUserAccount(authentication);

      // Logout the user and clear the session
      new SecurityContextLogoutHandler().logout(request, response, authentication);

      redirectAttributes.addFlashAttribute(
          "message", "Your account has been successfully deleted.");
      redirectAttributes.addFlashAttribute("messageType", "success");

    } catch (Exception e) {

      redirectAttributes.addFlashAttribute(
          "message", "Failed to delete your account. Please try again.");
      redirectAttributes.addFlashAttribute("messageType", "danger");
    }

    return "redirect:/v1/";
  }
}
