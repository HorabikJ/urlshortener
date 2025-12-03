package pl.jacekhorabik.urlshortener.pages.mainpage;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class ShortenUrlValidator {

  @Value("${app.external-base-url}")
  private URL appExternalBaseUrl;

  boolean isValidUrl(String urlForShortening) {
    try {
      if (urlForShortening == null) {
        return false;
      }
      URL url = new URI(urlForShortening).toURL();
      if (appExternalBaseUrl.getHost().equals(url.getHost())) {
        return false;
      }
      return "http".equals(url.getProtocol()) || "https".equals(url.getProtocol());
    } catch (URISyntaxException | MalformedURLException | IllegalArgumentException e) {
      return false;
    }
  }
}
