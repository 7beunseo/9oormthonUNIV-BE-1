package goorm.backend.work1.service;

import goorm.backend.work1.domain.Role;
import goorm.backend.work1.domain.UserEntity;
import goorm.backend.work1.dto.user.SignUpDTO;
import goorm.backend.work1.exception.DuplicateUsernameException;
import goorm.backend.work1.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public SignUpDTO.Res signUp(SignUpDTO req) {
        String username = req.getUsername();

        // 같은 username이 존재하는지 확인
        Boolean isExist = userRepository.existsByUsername(username);
        if (isExist) {
            throw new DuplicateUsernameException("중복된 아이디가 존재합니다.");
        }

        // 비밀번호 암호화
        String password = bCryptPasswordEncoder.encode(req.getPassword());
        Role role = req.getRole();

        UserEntity user = UserEntity.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();

        userRepository.save(user);
        return SignUpDTO.Res.mapEntityToDTO(user);
    }
}
