package com.travelock.server.converter;

import com.travelock.server.domain.FullCourse;
import com.travelock.server.dto.FullCourseResponseDTO;

import java.util.ArrayList;
import java.util.List;

// Entity를 DTO로 변환하는 클래스
public class DTOConverter {

    /**
     * FullCourse Entity to Response DTO
     */
    public static FullCourseResponseDTO toFullCourseResponseDTO(FullCourse fullCourse) {
        return new FullCourseResponseDTO(
                fullCourse.getFullCourseId(),
                fullCourse.getMember().getMemberId(),
                fullCourse.getMember().getNickName(),
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
}
