package goorm.backend.work1.dto.user;

import goorm.backend.work1.domain.Role;
import goorm.backend.work1.domain.UserEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class SignUpDTO {
    @NotEmpty(message = "아이디는 필수 입력 값입니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @NotNull(message = "권한은 필수 입력 값입니다.")
    private Role role;


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Res {
        private Long id;
        private String username;
        private Role role;

        public static Res mapEntityToDTO(UserEntity entity) {
            return Res.builder()
                    .id(entity.getId())
                    .username(entity.getUsername())
                    .role(entity.getRole())
                    .build();
        }
    }
}
