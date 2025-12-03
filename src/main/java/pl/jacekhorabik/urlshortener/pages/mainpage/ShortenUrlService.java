package pl.jacekhorabik.urlshortener.pages.mainpage;

import io.seruco.encoding.base62.Base62;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.jacekhorabik.urlshortener.pages.common.dto.UserAuthentication;
import pl.jacekhorabik.urlshortener.pages.common.entity.UrlEntity;
import pl.jacekhorabik.urlshortener.pages.common.entity.UrlRepository;

@RequiredArgsConstructor
@Service
class ShortenUrlService {

  private static final int HASH_LENGTH = 7;

  private final Base62 base62;
  private final UrlRepository urlRepository;

  @Transactional
  UrlEntity shortenUrl(final String url, final UserAuthentication userAuthentication)
      throws DecoderException {
    return shortenUrl(url, StringUtils.EMPTY, userAuthentication);
  }

  @Transactional
  void deleteUserUrlByHash(final String hash, final UserAuthentication userAuthentication) {
    if (userAuthentication.isAuthenticated()
        && urlRepository.existsByHashAndUserId(hash, userAuthentication.getUserId())) {
      urlRepository.deleteUrlEntityByHash(hash);
    }
  }

  Optional<UrlEntity> findUrlByHash(final String hash) {
    return urlRepository.findUrlEntityByHash(hash);
  }

  Page<UrlEntity> findUrlsByUserId(final String userId, final PageRequest pageRequest) {
    return urlRepository.findUrlEntityByUserId(userId, pageRequest);
  }

  private UrlEntity shortenUrl(
      final String url,
      final String hashCollisionProtector,
      final UserAuthentication userAuthentication)
      throws DecoderException {
    final String urlForHashing = url.concat(hashCollisionProtector);
    final String urlSha1Hash = DigestUtils.sha1Hex(urlForHashing);
    final byte[] urlHexadecimalBytes = Hex.decodeHex(urlSha1Hash);
    final String urlBase62Encoded = new String(base62.encode(urlHexadecimalBytes));
    final String urlBase62Substring = urlBase62Encoded.substring(0, HASH_LENGTH);
    final Optional<UrlEntity> urlEntity = urlRepository.findUrlEntityByHash(urlBase62Substring);
    if (urlEntity.isPresent()) {
      return shortenUrl(url, UUID.randomUUID().toString(), userAuthentication);
    } else {
      return userAuthentication.isAuthenticated()
          ? urlRepository.saveAndFlush(
              new UrlEntity(urlBase62Substring, url, userAuthentication.getUserId()))
          : urlRepository.saveAndFlush(new UrlEntity(urlBase62Substring, url));
    }
  }
}
