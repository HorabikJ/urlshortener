package pl.jacekhorabik.urlshortener.userregisterlogin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class UserRegisterLoginService {

  private final UserRepository userRepository;

  @Transactional
  void registerUser(final UserCredentialsDTO userCredentialsDTO) {
    userRepository.saveAndFlush(
        new UserEntity(userCredentialsDTO.email(), userCredentialsDTO.password()));
  }
}
