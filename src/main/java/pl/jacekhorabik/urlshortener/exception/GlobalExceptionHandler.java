package pl.jacekhorabik.urlshortener.exception;

import static pl.jacekhorabik.urlshortener.pages.common.view.RedirectView.REDIRECT;
import static pl.jacekhorabik.urlshortener.pages.common.view.View.NOT_FOUND;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Slf4j
class GlobalExceptionHandler {

  // todo implement handling different types of exceptions, like 500 server error

  @ExceptionHandler(value = Exception.class)
  public ModelAndView exceptionHandler(HttpServletRequest req, Exception e) {
    log.error("error when performing request. Request uri '{}'", req.getRequestURI(), e);
    return new ModelAndView(REDIRECT.to(NOT_FOUND));
  }
}
