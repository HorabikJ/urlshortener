package pl.jacekhorabik.urlshortener.mainpage;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "urls")
//todo use lombok here
class UrlEntity {

    @Id
    private String hash;
    
    private String url;

    protected UrlEntity(String hash, String url) {
        this.hash = hash;
        this.url = url;
    }

    protected UrlEntity() {
    }

    public String getHash() {
        return hash;
    }

    public String getUrl() {
        return url;
    }
}
