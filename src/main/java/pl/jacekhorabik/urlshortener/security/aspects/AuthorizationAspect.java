package pl.jacekhorabik.urlshortener.security.aspects;

import java.util.Arrays;
import java.util.Objects;
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
          "execution(@pl.jacekhorabik.urlshortener.security.aspects.AddAuthorizationToModelAndView "
              + "org.springframework.web.servlet.ModelAndView *(..))",
      returning = "modelAndView")
  public void addAuthorizationToModelAndView(
      @NotNull JoinPoint joinPoint, ModelAndView modelAndView) {
    final Object[] args = joinPoint.getArgs();
    Arrays.stream(args)
        .filter(o -> o instanceof OAuth2AuthenticationToken)
        .map((t -> (OAuth2AuthenticationToken) t))
        .findFirst()
        .ifPresentOrElse(
            t -> {
              modelAndView
                  .getModel()
                  .put("name", ((OidcUser) t.getPrincipal()).getPreferredUsername());
              modelAndView.addObject("isAuthenticated", t.isAuthenticated());
              modelAndView.addObject(
                  "isNice",
                  t.getAuthorities().stream()
                      .anyMatch(authority -> Objects.equals("NICE", authority.getAuthority())));
            },
            () -> {
              modelAndView.getModel().put("name", null);
              modelAndView.addObject("isAuthenticated", false);
              modelAndView.addObject("isNice", false);
            });
    System.out.println("lala");
  }
}
