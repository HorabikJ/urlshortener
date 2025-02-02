package pl.jacekhorabik.urlshortener.shortenurl;

import io.seruco.encoding.base62.Base62;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
class UrlShorteningService {

  private final Base62 base62;
  private final UrlRepository urlRepository;

  @Transactional
  UrlEntity shortenUrl(final UrlDTO url) throws DecoderException {
    return shortenUrl(url.url(), StringUtils.EMPTY);
  }

  private UrlEntity shortenUrl(final String url, final String hashCollisionProtector)
      throws DecoderException {
    final String urlForHashing = url.concat(hashCollisionProtector);
    final String urlSha1Hash = DigestUtils.sha1Hex(urlForHashing);
    final byte[] urlHexadecimalBytes = Hex.decodeHex(urlSha1Hash);
    final String urlBase62Encoded = new String(base62.encode(urlHexadecimalBytes));
    final String urlBase62Substring = urlBase62Encoded.substring(0, 7);
    final Optional<UrlEntity> urlEntity = urlRepository.findUrlEntityByHash(urlBase62Substring);
    if (urlEntity.isPresent()) {
        return shortenUrl(url, UUID.randomUUID().toString());
    } else {
      return urlRepository.saveAndFlush(new UrlEntity(urlBase62Substring, url));
    }
  }

  Optional<UrlEntity> findUrlByHash(final String hash) {
    return urlRepository.findUrlEntityByHash(hash);
  }
  
}
