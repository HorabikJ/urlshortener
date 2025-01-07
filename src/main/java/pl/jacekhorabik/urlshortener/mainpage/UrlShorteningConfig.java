package pl.jacekhorabik.urlshortener.mainpage;

import io.seruco.encoding.base62.Base62;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UrlShorteningConfig {

    @Bean
    Base62 base62() {
        return Base62.createInstance();
    }

}
