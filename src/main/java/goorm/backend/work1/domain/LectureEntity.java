package goorm.backend.work1.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor()
@AllArgsConstructor()
public class LectureEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id") // lecture_id로 변경
    private Long id; // 자동 생성 pk

    // 강의명
    private String lectureName;

    // 강의 시간
    private LocalTime startTime;
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY) // 관계 설정
    @JoinColumn(name = "professor_id", nullable = false) // professor_id 외래 키로 변경
    private UserEntity professor;

    // 수강 가능 인원수
    private Integer maxNum;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistrationEntity> registrations;
}

