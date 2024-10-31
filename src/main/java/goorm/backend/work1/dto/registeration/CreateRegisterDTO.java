package goorm.backend.work1.dto.registeration;

import goorm.backend.work1.domain.LectureEntity;
import goorm.backend.work1.domain.RegistrationEntity;
import goorm.backend.work1.domain.RegistrationStatus;
import goorm.backend.work1.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreateRegisterDTO {
    private Long lectureId;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Res {
        private Long lectureId;
        private Long userId;
        private LocalDateTime createdAt;
        private RegistrationStatus status;

        public static CreateRegisterDTO.Res mapEntityToDTO(RegistrationEntity entity) {
            return Res.builder()
                    .lectureId(entity.getLecture().getId())
                    .userId(entity.getUser().getId())
                    .createdAt(entity.getCreatedAt())
                    .status(entity.getStatus())
                    .build();
        }
    }
}
