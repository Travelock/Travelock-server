package com.travelock.server;

import com.travelock.server.config.QueryDslConfig;
import com.travelock.server.domain.DailyCourse;
import com.travelock.server.domain.FullAndDailyCourseConnect;
import com.travelock.server.domain.FullCourse;
import com.travelock.server.domain.Member;
import com.travelock.server.dto.course.daily_create.DailyCourseCreateDto;
import com.travelock.server.dto.course.daily_create.FullBlockDto;
import com.travelock.server.dto.course.daily_create.SmallBlockDto;
import com.travelock.server.exception.base_exceptions.BadRequestException;
import com.travelock.server.exception.base_exceptions.ResourceNotFoundException;
import com.travelock.server.repository.DailyCourseRepository;
import com.travelock.server.repository.FullAndDailyCourseConnectRepository;
import com.travelock.server.repository.FullBlockRepository;
import com.travelock.server.repository.MemberRepository;
import com.travelock.server.service.DailyCourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@ExtendWith(SpringExtension.class)
//@WebMvcTest(DailyCourseService.class)  //-- 컨트롤러 테스트에 사용
@Import(QueryDslConfig.class)
@SpringBootTest
public class DailyCourseServiceTest {

    @Autowired
    private DailyCourseService dailyCourseService; // 실제로 테스트할 서비스 클래스

    @MockBean
    private FullBlockRepository fullBlockRepository; // 모킹된 레포지토리

    @MockBean
    private FullAndDailyCourseConnectRepository fullAndDailyCourseConnectRepository; // 모킹된 레포지토리

    @MockBean
    private DailyCourseRepository dailyCourseRepository; // 모킹된 레포지토리

    @MockBean
    private MemberRepository memberRepository; // 모킹된 레포지토리

    // 필요한 다른 모킹된 레포지토리와 객체들

    private DailyCourseCreateDto createDto;

    @BeforeEach
    public void setUp() {
        // 테스트에 사용할 더미 DTO 객체 초기화
        createDto = new DailyCourseCreateDto();
        // DTO 내부 데이터 설정
        // 필요한 데이터를 세팅합니다. 예:
        FullBlockDto fullBlockDto = new FullBlockDto();
        fullBlockDto.setBigBlockId(1L);
        fullBlockDto.setMiddleBlockId(2L);
        SmallBlockDto smallBlockDto = new SmallBlockDto();
        smallBlockDto.setMapX("100.0");
        smallBlockDto.setMapY("200.0");
        smallBlockDto.setPlaceId("placeId123");
        fullBlockDto.setSmallBlockDto(smallBlockDto);

        createDto.setMemberId(1L);
        createDto.setFullBlockDtoList(List.of(fullBlockDto));
        createDto.setFullCourseId(1L);
        createDto.setDayNum(1);

        // Mock Repository가 동작하도록 설정
        Member mockMember = new Member();
        mockMember.setMemberId(1L);

        FullCourse mockFullCourse = new FullCourse();
        mockFullCourse.setFullCourseId(1L);

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(mockMember));
        when(fullBlockRepository.saveAll(anyList())).thenReturn(new ArrayList<>()); // 비어있는 리스트로 리턴 모킹
        when(fullAndDailyCourseConnectRepository.save(any())).thenReturn(new FullAndDailyCourseConnect());
        when(dailyCourseRepository.save(any())).thenReturn(new DailyCourse());
    }

    @Test
    public void testSaveDailyCourseSuccess() {
        // When: 테스트할 메서드를 실행했을 때
        DailyCourse result = dailyCourseService.saveDailyCourse(createDto);

        // Then: 제대로 동작했는지 검증
        assertThat(result).isNotNull(); // 결과가 null이 아닌지 확인
        verify(fullBlockRepository, times(1)).saveAll(anyList()); // 배치 저장이 한 번 호출되었는지 확인
        verify(fullAndDailyCourseConnectRepository, times(1)).save(any()); // 연결 객체 저장이 한 번 호출되었는지 확인
        verify(dailyCourseRepository, times(1)).save(any()); // DailyCourse가 한 번 저장되었는지 확인
    }

    @Test
    public void testSaveDailyCourseWithNullDto() {
        // Given: createDto가 null일 경우
        createDto = null;

        // Then: 예외가 발생하는지 확인
        assertThatThrownBy(() -> dailyCourseService.saveDailyCourse(createDto))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    public void testSaveDailyCourseMemberNotFound() {
        // Given: Member가 존재하지 않는 경우
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Then: 예외가 발생하는지 확인
        assertThatThrownBy(() -> dailyCourseService.saveDailyCourse(createDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Member not found");
    }
}
