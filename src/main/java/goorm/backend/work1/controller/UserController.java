package goorm.backend.work1.controller;

import goorm.backend.work1.code.SuccessCode;
import goorm.backend.work1.dto.response.ResponseDTO;
import goorm.backend.work1.dto.user.SignUpDTO;
import goorm.backend.work1.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO> registerUser(@Valid @RequestBody SignUpDTO signUpDTO) {
        SignUpDTO.Res res = userService.signUp(signUpDTO);
        return ResponseEntity
                .status(SuccessCode.SUCCESS_REGISTER.getStatus().value())
                .body(new ResponseDTO<>(SuccessCode.SUCCESS_REGISTER, res));
    }
}
