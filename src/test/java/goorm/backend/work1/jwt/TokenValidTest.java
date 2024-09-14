package goorm.backend.work1.jwt;

import goorm.backend.work1.domain.Role;
import goorm.backend.work1.dto.user.SignUpDTO;
import goorm.backend.work1.repository.UserRepository;
import goorm.backend.work1.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TokenValidTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    JWTUtil jwtUtil;

    @Value("${spring.jwt.secret}")
    private String secret;

    @Test
    void 유효한_토큰_검사() {
        // given
        SignUpDTO req = new SignUpDTO("eunseo", "1234", Role.ROLE_USER);
        SignUpDTO.Res res = userService.signUp(req);
        String jwt = jwtUtil.createJwt("accessToken", res.getUsername(), res.getRole().toString(), 1000000L);

        // when
        String username = jwtUtil.getUsername(jwt);

        // then
        assertTrue(userRepository.findByUsername(username).isPresent());
    }

    @Test
    void 유효하지_않은_토큰_검사() {
        // given
        String invalidToken = "invalid-jwt-token";

        // when & then
        assertThrows(MalformedJwtException.class, () -> jwtUtil.getUsername(invalidToken));
    }

    @Test
    void 만료된_토큰_검사() {
        // given
        SignUpDTO req = new SignUpDTO("eunseo", "1234", Role.ROLE_USER);
        SignUpDTO.Res res = userService.signUp(req);
        String expiredToken = jwtUtil.createJwt("accessToken", res.getUsername(), res.getRole().toString(), 1L);

        // when - 2초 대기
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // then
        assertThrows(ExpiredJwtException.class, () -> jwtUtil.getUsername(expiredToken));
    }

}
