package goorm.backend.work1.dto.response;

import goorm.backend.work1.code.SuccessCode;
import lombok.Data;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
public class ResponseDTO<T> {
    private Integer status;
    private String code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    // 데이터가 있을 경우
    public ResponseDTO(SuccessCode successCode, T data) {
        this.status = successCode.getStatus().value();
        this.code = successCode.name();
        this.message = successCode.getMessage();
        this.data = data;
    }

    // 데이터가 없을 경우
    public ResponseDTO(SuccessCode successCode) {
        this.status = successCode.getStatus().value();
        this.code = successCode.name();
        this.message = successCode.getMessage();
    }
}
