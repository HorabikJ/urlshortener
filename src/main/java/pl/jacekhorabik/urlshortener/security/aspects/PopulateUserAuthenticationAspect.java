package pl.jacekhorabik.urlshortener.security.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import pl.jacekhorabik.urlshortener.pages.common.dto.UserAuthentication;

@Aspect
@Component
class PopulateUserAuthenticationAspect {

  /**
   * Uses {@link PopulateUserAuthentication annotation} and {@link UserAuthentication}.
   *
   * @param joinPoint
   * @return
   * @throws Throwable
   */
  @Around(
      "execution(@pl.jacekhorabik.urlshortener.security.aspects.PopulateUserAuthentication "
          + "* *.*(..,pl.jacekhorabik.urlshortener.pages.common.dto.UserAuthentication,..))")
  Object populateUserDataFromAuthentication(ProceedingJoinPoint joinPoint) throws Throwable {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    Object[] args = joinPoint.getArgs();
    for (Object arg : args) {
      if (arg instanceof UserAuthentication userAuthentication) {
        if (authentication instanceof OAuth2AuthenticationToken oAuth2Token) {
          OAuth2User oAuth2User = oAuth2Token.getPrincipal();
          userAuthentication.setUserId(oAuth2User.getAttribute("sub"));
          userAuthentication.setPreferredUsername(oAuth2User.getAttribute("preferred_username"));
          userAuthentication.setAuthenticated(oAuth2Token.isAuthenticated());
        } else {
          userAuthentication.setAuthenticated(false);
        }
      }
    }

    return joinPoint.proceed(args);
  }
}
