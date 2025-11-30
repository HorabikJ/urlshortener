package pl.jacekhorabik.urlshortener.pages.common.entity;

import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.Getter;
import org.hibernate.annotations.Generated;

@MappedSuperclass
@Getter
public class BaseEntity {

  @Generated private Instant createdAt;
  @Generated private Instant updatedAt;
}
