package goorm.backend.work1.repository;

import goorm.backend.work1.domain.LectureEntity;
import goorm.backend.work1.domain.RegistrationEntity;
import goorm.backend.work1.domain.RegistrationStatus;
import goorm.backend.work1.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {

    boolean existsByUserAndLecture(UserEntity user, LectureEntity lecture);

    @Query("SELECT COUNT(r) > 0 " +
            "FROM RegistrationEntity r " +
            "WHERE r.user = :user " +
            "AND r.status = 'COMPLETED' " +
            "AND r.lecture.startTime < :endTime " +
            "AND r.lecture.endTime > :startTime")
    boolean existsByUserAndLecture_TimeOverlap(
            @Param("user") UserEntity user,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    List<RegistrationEntity> findByUserAndStatus(UserEntity user, RegistrationStatus registrationStatus);
}
