package pl.jacekhorabik.urlshortener.pages.common;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.jacekhorabik.urlshortener.common.entity.BaseEntity;

@Entity
@Table(name = "urls")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UrlEntity extends BaseEntity {

  @Id private String hash; // todo add hibernate validation
  private String url;
  private String userId;

  public UrlEntity(final String hash, final String url) {
    this.hash = hash;
    this.url = url;
    this.userId = null;
  }

  public UrlEntity(final String hash, final String url, final String userId) {
    this.hash = hash;
    this.url = url;
    this.userId = userId;
  }
}
