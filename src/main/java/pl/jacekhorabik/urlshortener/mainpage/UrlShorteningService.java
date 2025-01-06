package pl.jacekhorabik.urlshortener.mainpage;

import io.seruco.encoding.base62.Base62;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
class UrlShorteningService {

    private final Base62 base62;
    private final UrlRepository urlRepository;
    
    @Transactional
    String shortenUrl(String url) throws DecoderException {
        String urlSha1Hash = DigestUtils.sha1Hex(url);
        byte[] urlHexadecimalBytes = Hex.decodeHex(urlSha1Hash);
        String urlBase62Shortage = new String(base62.encode(urlHexadecimalBytes));
        UrlEntity savedUrl = urlRepository.save(new UrlEntity(urlBase62Shortage, urlBase62Shortage));
        return savedUrl.getHash();
    }

}
