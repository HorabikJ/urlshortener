package pl.jacekhorabik.urlshortener.userregisterlogin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class UserRegisterLoginService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  void registerUser(final UserCredentialsDTO userCredentialsDTO) {
    if (!userRepository.existsByEmail(userCredentialsDTO.email())) {
      userRepository.saveAndFlush(
          new UserEntity(
              userCredentialsDTO.email(), passwordEncoder.encode(userCredentialsDTO.password())));
    } else {
      throw new IllegalArgumentException("Email already in registered");
    }
  }
}
