package goorm.backend.work1.repository;

import goorm.backend.work1.domain.LectureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectorRepository extends JpaRepository<LectureEntity, Long> {

}
