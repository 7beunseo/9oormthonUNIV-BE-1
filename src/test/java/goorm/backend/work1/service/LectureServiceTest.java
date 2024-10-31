package goorm.backend.work1.service;

import goorm.backend.work1.domain.LectureEntity;
import goorm.backend.work1.domain.RegistrationEntity;
import goorm.backend.work1.domain.RegistrationStatus;
import goorm.backend.work1.domain.UserEntity;
import goorm.backend.work1.dto.registeration.CreateRegisterDTO;
import goorm.backend.work1.repository.LectorRepository;
import goorm.backend.work1.repository.RegistrationRepository;
import goorm.backend.work1.repository.UserRepository;
import goorm.backend.work1.service.LectureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LectureServiceTest {

    @InjectMocks
    private LectureService lectureService;

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private LectorRepository lectureRepository;

    @Mock
    private UserRepository userRepository;

    private UserEntity user;
    private LectureEntity lecture;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // 테스트용 유저와 강의 객체 생성
        user = UserEntity.builder()
                .id(1L)
                .username("testUser")
                .build();

        lecture = LectureEntity.builder()
                .id(1L)
                .lectureName("테스트 강의")
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(12, 0))
                .maxNum(30)
                .registrations(new ArrayList<>())
                .build();
    }

    // 수강신청 성공 테스트
    @Test
    public void testCourseRegistration_Success() {
        CreateRegisterDTO dto = new CreateRegisterDTO(1L);

        // Mock 설정: 유저와 강의 정보를 찾을 수 있고, 중복 수강신청이나 시간대 중복이 발생하지 않음
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(lectureRepository.findById(1L)).thenReturn(Optional.of(lecture));
        when(registrationRepository.existsByUserAndLecture(user, lecture)).thenReturn(false);
        when(registrationRepository.existsByUserAndLecture_TimeOverlap(user, lecture.getStartTime(), lecture.getEndTime())).thenReturn(false);

        // 수강신청 실행
        lectureService.courseRegistration(dto, "testUser");

        // 수강신청이 성공적으로 저장되었는지 확인
        verify(registrationRepository, times(1)).save(any(RegistrationEntity.class));
    }

    // 동일 강좌에 대한 중복 수강신청 예외 처리 테스트
    @Test
    public void testCourseRegistration_DuplicateLecture_ExceptionThrown() {
        CreateRegisterDTO dto = new CreateRegisterDTO(1L);

        // Mock 설정: 동일 강좌에 대한 중복 수강신청 존재
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(lectureRepository.findById(1L)).thenReturn(Optional.of(lecture));
        when(registrationRepository.existsByUserAndLecture(user, lecture)).thenReturn(true);

        // 중복 수강신청 시 IllegalArgumentException 발생 여부 확인
        assertThrows(IllegalArgumentException.class, () -> lectureService.courseRegistration(dto, "testUser"));
    }

    // 동일 시간대 다른 강좌 수강신청 예외 처리 테스트
    @Test
    public void testCourseRegistration_TimeOverlap_ExceptionThrown() {
        CreateRegisterDTO dto = new CreateRegisterDTO(1L);

        // Mock 설정: 시간대 중복 발생
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(lectureRepository.findById(1L)).thenReturn(Optional.of(lecture));
        when(registrationRepository.existsByUserAndLecture(user, lecture)).thenReturn(false);
        when(registrationRepository.existsByUserAndLecture_TimeOverlap(user, lecture.getStartTime(), lecture.getEndTime())).thenReturn(true);

        // 시간대 중복 시 IllegalArgumentException 발생 여부 확인
        assertThrows(IllegalArgumentException.class, () -> lectureService.courseRegistration(dto, "testUser"));
    }

    // 수강 취소 성공 시나리오 테스트
    @Test
    public void testCourseCancel_Success() {
        RegistrationEntity registration = RegistrationEntity.builder()
                .id(1L)
                .user(user)
                .lecture(lecture)
                .status(RegistrationStatus.COMPLETED)
                .build();

        // 취소할 수강 신청이 존재
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));

        // 수강 취소 실행
        lectureService.courseCancel(1L);

        // 수강 상태가 CANCEL로 변경되었는지 확인
        assertEquals(RegistrationStatus.CANCEL, registration.getStatus());
        verify(registrationRepository, times(1)).save(registration);
    }

    // 학생의 수강신청 목록 조회 테스트
    @Test
    public void testGetStudentRegistrations() {
        RegistrationEntity registration = RegistrationEntity.builder()
                .user(user)
                .lecture(lecture)
                .status(RegistrationStatus.COMPLETED)
                .build();

        // Mock 설정: 유저 및 수강 신청 목록 조회
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(registrationRepository.findByUserAndStatus(user, RegistrationStatus.COMPLETED))
                .thenReturn(List.of(registration));

        // 수강신청 목록 조회 실행
        List<RegistrationEntity> registrations = lectureService.getStudentRegistrations(1L);

        // 조회 결과 검증
        assertEquals(1, registrations.size());
        assertEquals(RegistrationStatus.COMPLETED, registrations.get(0).getStatus());
    }

    // 수강신청 가능 강좌만 조회하는 기능 테스트
    @Test
    public void testGetAllLectures_AvailableOnly() {
        LectureEntity lecture1 = LectureEntity.builder()
                .id(1L)
                .lectureName("Lecture 1")
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(11, 0))
                .maxNum(30)
                .registrations(new ArrayList<>())
                .build();

        LectureEntity lecture2 = LectureEntity.builder()
                .id(2L)
                .lectureName("Lecture 2")
                .startTime(LocalTime.of(12, 0))
                .endTime(LocalTime.of(14, 0))
                .maxNum(20)
                .registrations(new ArrayList<>())
                .build();

        // 정원 내에 신청된 강의만 필터링하도록 설정
        lecture1.getRegistrations().add(new RegistrationEntity());
        lecture2.getRegistrations().add(new RegistrationEntity());

        when(lectureRepository.findAll()).thenReturn(List.of(lecture1, lecture2));

        // 전체 강좌 중 수강신청 가능 강좌만 조회
        List<LectureEntity> availableLectures = lectureService.getAllLectures(true);

        // 수강신청 가능 강좌가 올바르게 필터링되었는지 검증
        assertEquals(2, availableLectures.size());
    }
}
