package goorm.backend.work1.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import goorm.backend.work1.code.ErrorCode;
import lombok.Data;

import java.util.Map;

@Data
public class ErrorResponseDTO {
    private int status;
    private String error;
    private String code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> errors;

    public ErrorResponseDTO(ErrorCode errorCode) {
        this.status = errorCode.getStatus().value();
        this.error = errorCode.getStatus().name();
        this.code = errorCode.name();
        this.message = errorCode.getMessage();
    }

    public ErrorResponseDTO(ErrorCode errorCode, Map<String, String> errors) {
        this.status = errorCode.getStatus().value();
        this.error = errorCode.getStatus().name();
        this.code = errorCode.name();
        this.message = errorCode.getMessage();
        this.errors = errors;
    }
}
