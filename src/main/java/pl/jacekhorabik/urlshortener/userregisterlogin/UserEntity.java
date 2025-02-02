package pl.jacekhorabik.urlshortener.userregisterlogin;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UserEntity {

  @Id private String id;
  private String email;
  private String password;
  @Generated private Instant createdAt;
  @Generated private Instant updatedAt;

  UserEntity(final String email, final String password) {
    //  todo maybe some custom hibernate generator for this?
    this.id = UUID.randomUUID().toString();
    this.email = email;
    this.password = password;
  }
}
