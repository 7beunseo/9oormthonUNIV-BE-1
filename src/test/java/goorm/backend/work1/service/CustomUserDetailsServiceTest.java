package goorm.backend.work1.service;

import goorm.backend.work1.domain.Role;
import goorm.backend.work1.dto.user.SignUpDTO;
import goorm.backend.work1.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CustomUserDetailsServiceTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void 아이디로_유저_조회_성공() {
        // given
        SignUpDTO req = new SignUpDTO("eunseo", "1234", Role.ROLE_USER);

        // when
        SignUpDTO.Res res = userService.signUp(req);

        // then
        UserDetails findUser = customUserDetailsService.loadUserByUsername("eunseo");
        assertNotNull(findUser);
    }

    @Test
    void 아이디로_유저_조회_실패() {
        // given
        SignUpDTO req = new SignUpDTO("eunseo", "1234", Role.ROLE_USER);

        // when
        SignUpDTO.Res res = userService.signUp(req);

        // then
        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("eunseo12"));
    }

}