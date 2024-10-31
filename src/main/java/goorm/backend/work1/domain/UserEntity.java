package goorm.backend.work1.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USERS")
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id; // 자동 생성 pk

    @Column(name = "username")
    private String username; // 아이디

    @Column(name = "password")
    private String password; // 비밀번호

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role; // 일반 유저와 관리자 계정 구분

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistrationEntity> registrations;
}

