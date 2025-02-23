package pl.jacekhorabik.urlshortener.security.aspects;

import java.util.Optional;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Aspect
@Component
class AuthorizationAspect {

  private enum AuthenticationAttributes {
    USERNAME("username"),
    IS_AUTHENTICATED("isAuthenticated");

    private final String attribute;

    AuthenticationAttributes(String attribute) {
      this.attribute = attribute;
    }

    @Override
    public String toString() {
      return attribute;
    }
  }

  @AfterReturning(
      value =
          "execution(@pl.jacekhorabik.urlshortener.security.aspects.AddUserDataToModel "
              + "org.springframework.web.servlet.ModelAndView *(..))",
      returning = "modelAndView")
  void addUserDataToModel(ModelAndView modelAndView) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Optional.ofNullable(authentication)
        .filter(o -> o instanceof OAuth2AuthenticationToken)
        .map((t -> (OAuth2AuthenticationToken) t))
        .ifPresentOrElse(
            t -> {
              modelAndView.addObject(
                  AuthenticationAttributes.USERNAME.toString(),
                  ((OidcUser) t.getPrincipal()).getPreferredUsername());
              modelAndView.addObject(
                  AuthenticationAttributes.IS_AUTHENTICATED.toString(), t.isAuthenticated());
            },
            () -> {
              modelAndView.addObject(AuthenticationAttributes.USERNAME.toString(), null);
              modelAndView.addObject(AuthenticationAttributes.IS_AUTHENTICATED.toString(), false);
            });
  }
}
