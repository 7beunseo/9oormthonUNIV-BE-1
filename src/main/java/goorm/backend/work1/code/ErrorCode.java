package goorm.backend.work1.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    /**
     * 400 BAD_REQUEST: 잘못된 요청
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    /**
     * 409
     */
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "중복된 유저 이름입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
