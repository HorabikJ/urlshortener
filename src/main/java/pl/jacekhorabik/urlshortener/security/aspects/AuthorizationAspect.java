package pl.jacekhorabik.urlshortener.security.aspects;

import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Aspect
@Component
class AuthorizationAspect {

  @AfterReturning(
      value =
          "execution(@pl.jacekhorabik.urlshortener.security.aspects.AddUserDataToModel "
              + "org.springframework.web.servlet.ModelAndView *(..))",
      returning = "modelAndView")
  public void addUserDataToModel(@NotNull JoinPoint joinPoint, ModelAndView modelAndView) {
    final Object[] args = joinPoint.getArgs();
    Arrays.stream(args)
        .filter(o -> o instanceof OAuth2AuthenticationToken)
        .map((t -> (OAuth2AuthenticationToken) t))
        .findFirst()
        .ifPresentOrElse(
            t -> {
              modelAndView
                  .getModel()
                      .put("username", ((OidcUser) t.getPrincipal()).getPreferredUsername());
              modelAndView.addObject("isAuthenticated", t.isAuthenticated());
            },
            () -> {
                modelAndView.getModel().put("username", null);
              modelAndView.addObject("isAuthenticated", false);
            });
  }
}
