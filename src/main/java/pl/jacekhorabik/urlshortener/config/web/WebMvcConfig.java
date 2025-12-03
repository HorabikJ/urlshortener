package pl.jacekhorabik.urlshortener.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.jacekhorabik.urlshortener.pages.common.view.View;

@Configuration
class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController(View.NOT_FOUND.getViewPath()).setViewName(View.NOT_FOUND.toString());
  }
}
