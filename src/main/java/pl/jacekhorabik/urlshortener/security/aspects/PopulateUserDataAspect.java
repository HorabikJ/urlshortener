package pl.jacekhorabik.urlshortener.security.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import pl.jacekhorabik.urlshortener.common.model.UserData;

@Aspect
@Component
@Slf4j
class PopulateUserDataAspect {

  /**
   * Uses {@link PopulateUserData annotation}
   *
   * @param joinPoint
   * @return
   * @throws Throwable
   */
  @Around(
      "execution(@pl.jacekhorabik.urlshortener.security.aspects.PopulateUserData "
          + "* *.*(..,pl.jacekhorabik.urlshortener.common.model.UserData,..))")
  Object populateUserDataFromAuthentication(ProceedingJoinPoint joinPoint) throws Throwable {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    Object[] args = joinPoint.getArgs();
    for (Object arg : args) {
      if (arg instanceof UserData userData) {
        if (authentication instanceof OAuth2AuthenticationToken oAuth2Token) {
          OAuth2User oAuth2User = oAuth2Token.getPrincipal();
          userData.setUserId(oAuth2User.getAttribute("sub"));
          userData.setPreferredUsername(oAuth2User.getAttribute("preferred_username"));
          userData.setEmail(oAuth2User.getAttribute("email"));
          userData.setAuthenticated(oAuth2Token.isAuthenticated());
        } else {
          userData.setAuthenticated(false);
        }
      }
    }

    return joinPoint.proceed(args);
  }
}
