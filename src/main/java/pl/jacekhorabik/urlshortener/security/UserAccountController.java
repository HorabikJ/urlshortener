// package pl.jacekhorabik.urlshortener.security;
//
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
// @Controller
// @RequestMapping("/v1/account")
// @RequiredArgsConstructor
// @Slf4j
// public class UserAccountController {
//
//  private final UserAccountService userAccountService;
//
//  /**
//   * Deletes the currently authenticated user's account from Keycloak. After successful deletion,
//   * the user is logged out and redirected to the main page.
//   *
//   * @param request the HTTP request
//   * @param response the HTTP response
//   * @param redirectAttributes attributes for redirect messages
//   * @return redirect to main page
//   */
//  @PostMapping("/delete")
//  public String deleteAccount(
//      HttpServletRequest request,
//      HttpServletResponse response,
//      RedirectAttributes redirectAttributes) {
//
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//    try {
//      // Delete user from Keycloak
//      userAccountService.deleteCurrentUserAccount(authentication);
//
//      // Logout the user and clear the session
//      new SecurityContextLogoutHandler().logout(request, response, authentication);
//
//      log.info("User account deleted successfully and user logged out");
//
//      redirectAttributes.addFlashAttribute(
//          "message", "Your account has been successfully deleted.");
//      redirectAttributes.addFlashAttribute("messageType", "success");
//
//    } catch (Exception e) {
//      log.error("Failed to delete user account", e);
//
//      redirectAttributes.addFlashAttribute(
//          "message", "Failed to delete your account. Please try again.");
//      redirectAttributes.addFlashAttribute("messageType", "danger");
//    }
//
//    return "redirect:/v1/";
//  }
// }
