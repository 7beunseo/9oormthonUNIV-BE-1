package goorm.backend.work1.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor()
@AllArgsConstructor()
public class RegistrationEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_id") // registration_id로 변경
    private Long id; // 자동 생성 pk

    // 상태
    private RegistrationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 외래 키
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false) // 외래 키
    private LectureEntity lecture;

    public void updateStatus(RegistrationStatus registrationStatus) {
        this.status = registrationStatus;
    }
}

