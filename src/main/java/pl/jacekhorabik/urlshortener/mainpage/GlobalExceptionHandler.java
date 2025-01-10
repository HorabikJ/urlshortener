package pl.jacekhorabik.urlshortener.mainpage;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
class GlobalExceptionHandler {

  @ExceptionHandler(value = NoResourceFoundException.class)
  public ModelAndView handleNoStaticResourceFound(HttpServletRequest req, Exception e) {
    return new ModelAndView(ViewName.NOT_FOUND.viewName());
  }
  // todo implement handling different types of exceptions

}
