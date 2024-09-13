package goorm.backend.work1.service;

import goorm.backend.work1.domain.Role;
import goorm.backend.work1.domain.UserEntity;
import goorm.backend.work1.dto.user.SignUpDTO;
import goorm.backend.work1.exception.DuplicateUsernameException;
import goorm.backend.work1.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void 유저_회원가입() {
        // given
        SignUpDTO req = new SignUpDTO("eunseo", "1234", Role.ROLE_USER);

        // when
        SignUpDTO.Res res = userService.signUp(req);

        // then
        Optional<UserEntity> findUser = userRepository.findById(res.getId());
        assertTrue(findUser.isPresent());

        UserEntity user = findUser.get();
        assertEquals(user.getUsername(), "eunseo");
        assertTrue(bCryptPasswordEncoder.matches("1234", user.getPassword()));
        assertEquals(user.getRole(), Role.ROLE_USER);
    }

    @Test
    void 중복_아이디_에러() {
        // given
        SignUpDTO req1 = new SignUpDTO("eunseo", "1234", Role.ROLE_USER);
        SignUpDTO req2 = new SignUpDTO("eunseo", "1234", Role.ROLE_USER);

        // when
        userService.signUp(req1);

        // then
        assertThrows(DuplicateUsernameException.class, () -> userService.signUp(req2));
    }
}