package com.travelock.server.converter;

import com.travelock.server.domain.DailyCourse;
import com.travelock.server.domain.FullCourse;
import com.travelock.server.dto.DailyCourseResponseDTO;
import com.travelock.server.dto.FullCourseResponseDTO;

import java.util.ArrayList;
import java.util.List;

// Entity를 DTO로 변환하는 클래스
public class DTOConverter {

    /**
     * 전체 일정 Entity to Response DTO
     */
    public static FullCourseResponseDTO toFullCourseResponseDTO(FullCourse fullCourse) {
        return new FullCourseResponseDTO(
                fullCourse.getFullCourseId(),
                fullCourse.getMember().getMemberId(),
//                fullCourse.getMember().getNickName(),
                fullCourse.getMember().getName(), // 닉네임 대체
                fullCourse.getTitle(),
                fullCourse.getFavoriteCount(),
                fullCourse.getScarpCount(),
                fullCourse.getDailyCourses()
        );
    }
    // 2개 이상의 FullCourse를 FullCourseResponseDTO로 변환
    public static List<FullCourseResponseDTO> toFullCourseResponseDTOList(List<FullCourse> fullCourses) {
        List<FullCourseResponseDTO> responseDTOList = new ArrayList<>();
        for (FullCourse fullCourse : fullCourses) {
            responseDTOList.add(toFullCourseResponseDTO(fullCourse));
        }
        return responseDTOList;
    }

    /**
     * 일자별 일정 Entity to Response DTO
     */
    public static DailyCourseResponseDTO toDailyCourseResponseDTO(DailyCourse dailyCourse) {
        return new DailyCourseResponseDTO(
                dailyCourse.getDailyCourseId(),
                dailyCourse.getDayNum(),
                dailyCourse.getFullCourse().getFullCourseId(),
                dailyCourse.getMember().getMemberId(),
                dailyCourse.getFavoriteCount(),
                dailyCourse.getScarpCount(),
                null
        );
    }
    // 2개 이상의 DailyCourse를 DailyCourseResponseDTO로 변환
    public static List<DailyCourseResponseDTO> toDailyCourseResponseDTOList(List<DailyCourse> dailyCourses) {
        List<DailyCourseResponseDTO> responseDTOList = new ArrayList<>();
        for (DailyCourse dailyCourse : dailyCourses) {
            responseDTOList.add(toDailyCourseResponseDTO(dailyCourse));
        }
        return responseDTOList;
    }
}
