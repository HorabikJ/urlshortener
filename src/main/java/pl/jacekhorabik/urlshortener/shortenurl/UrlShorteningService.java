package pl.jacekhorabik.urlshortener.shortenurl;

import io.seruco.encoding.base62.Base62;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.jacekhorabik.urlshortener.common.model.UserData;

@RequiredArgsConstructor
@Service
class UrlShorteningService {

  private static final int HASH_LENGTH = 7;

  private final Base62 base62;
  private final UrlRepository urlRepository;

  @Transactional
  //  todo change parameter of this method to UrlEntity
  UrlEntity shortenUrl(final UrlDTO url, final UserData userData) throws DecoderException {
    return shortenUrl(url.url(), StringUtils.EMPTY, userData);
  }

  private UrlEntity shortenUrl(
      final String url, final String hashCollisionProtector, final UserData userData)
      throws DecoderException {
    final String urlForHashing = url.concat(hashCollisionProtector);
    final String urlSha1Hash = DigestUtils.sha1Hex(urlForHashing);
    final byte[] urlHexadecimalBytes = Hex.decodeHex(urlSha1Hash);
    final String urlBase62Encoded = new String(base62.encode(urlHexadecimalBytes));
    final String urlBase62Substring = urlBase62Encoded.substring(0, HASH_LENGTH);
    final Optional<UrlEntity> urlEntity = urlRepository.findUrlEntityByHash(urlBase62Substring);
    if (urlEntity.isPresent()) {
      return shortenUrl(url, UUID.randomUUID().toString(), userData);
    } else {
      return userData.isAuthenticated()
          ? urlRepository.saveAndFlush(new UrlEntity(urlBase62Substring, url, userData.getUserId()))
          : urlRepository.saveAndFlush(new UrlEntity(urlBase62Substring, url));
    }
  }

  Optional<UrlEntity> findUrlByHash(final String hash) {
    return urlRepository.findUrlEntityByHash(hash);
  }

  List<UrlEntity> findUrlsByUserId(final String userId) {
    return urlRepository.findUrlEntityByUserId(userId);
  }
}
