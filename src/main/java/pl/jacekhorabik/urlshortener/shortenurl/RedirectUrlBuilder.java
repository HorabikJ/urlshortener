package pl.jacekhorabik.urlshortener.shortenurl;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class RedirectUrlBuilder {

  @Value("${app.external-base-url}")
  private String appExternalBaseUrl;

  URI buildRedirectUrl(final String hash) {
    final String redirectUrl = appExternalBaseUrl + "/v1/r/" + hash;
    return URI.create(redirectUrl);
  }
}
