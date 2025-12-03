package pl.jacekhorabik.urlshortener.pages.mainpage;

import java.net.URL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class RedirectUrlBuilder {

  @Value("${app.external-base-url}")
  private URL appExternalBaseUrl;

  String buildRedirectUrl(final String hash) {
    return appExternalBaseUrl.toString() + "/v1/r/" + hash;
  }
}
