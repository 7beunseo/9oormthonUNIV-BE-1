package goorm.backend.work1.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum SuccessCode {
    SUCCESS_REGISTER(HttpStatus.CREATED, "회원가입을 성공했습니다."),

    /**
     * 로그인
     */
    SUCCESS_LOGIN(HttpStatus.OK, "로그인을 성공했습니다. 헤더 토큰을 확인하세요."),
    ;

    private final HttpStatus status;
    private final String message;
}
