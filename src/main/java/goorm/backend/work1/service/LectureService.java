package goorm.backend.work1.service;

import goorm.backend.work1.domain.LectureEntity;
import goorm.backend.work1.domain.RegistrationEntity;
import goorm.backend.work1.domain.RegistrationStatus;
import goorm.backend.work1.domain.UserEntity;
import goorm.backend.work1.dto.registeration.CreateRegisterDTO;
import goorm.backend.work1.repository.LectorRepository;
import goorm.backend.work1.repository.RegistrationRepository;
import goorm.backend.work1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureService {
    private final RegistrationRepository registrationRepository;
    private final LectorRepository lectureRepository;
    private final UserRepository userRepository;

    // 수강신청
    public void courseRegistration(CreateRegisterDTO createRegisterDTO, String username) {
        // 유저와 강의 정보 조회
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        LectureEntity lecture = lectureRepository.findById(createRegisterDTO.getLectureId())
                .orElseThrow(() -> new IllegalArgumentException("강의를 찾을 수 없습니다."));

        // 동일한 강좌에 이미 등록한 경우 예외 처리
        if (registrationRepository.existsByUserAndLecture(user, lecture)) {
            throw new IllegalArgumentException("이미 수강신청한 강좌입니다.");
        }

        // 시간대 중복 확인
        if (registrationRepository.existsByUserAndLecture_TimeOverlap(user, lecture.getStartTime(), lecture.getEndTime())) {
            throw new IllegalArgumentException("동일 시간대에 다른 강좌가 이미 수강신청되었습니다.");
        }

        // 정원 확인
        if (lecture.getRegistrations().size() >= lecture.getMaxNum()) {
            throw new IllegalArgumentException("수강 정원이 초과되었습니다.");
        }

        // 수강신청 등록
        RegistrationEntity registration = RegistrationEntity.builder()
                .user(user)
                .lecture(lecture)
                .status(RegistrationStatus.COMPLETED)
                .build();
        registrationRepository.save(registration);
    }

    // 수강 취소
    public void courseCancel(Long registrationId) {
        RegistrationEntity registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 수강신청을 찾을 수 없습니다."));

        // 수강 상태를 CANCEL로 변경 후 저장
        registration.updateStatus(RegistrationStatus.CANCEL);
        registrationRepository.save(registration);
    }

    // 수강신청 목록 조회
    public List<RegistrationEntity> getStudentRegistrations(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        return registrationRepository.findByUserAndStatus(user, RegistrationStatus.COMPLETED);
    }

    // 모든 강좌 목록 조회 (필터링 포함)
    public List<LectureEntity> getAllLectures(Boolean availableOnly) {
        List<LectureEntity> lectures = lectureRepository.findAll();
        if (availableOnly) {
            return lectures.stream()
                    .filter(lecture -> lecture.getRegistrations().size() < lecture.getMaxNum())
                    .collect(Collectors.toList());
        }
        return lectures;
    }
}
