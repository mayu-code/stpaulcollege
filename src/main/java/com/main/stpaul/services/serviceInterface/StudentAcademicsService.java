package com.main.stpaul.services.serviceInterface;

import java.util.List;

import com.main.stpaul.dto.response.StudentAcademicsResponse;
import com.main.stpaul.entities.StudentAcademics;

public interface StudentAcademicsService {
    StudentAcademics addStudentAcademics(StudentAcademics studentAcademics);
    List<StudentAcademicsResponse> getAcademicsByStudent(String studentId);
    StudentAcademicsResponse getAcademicsById(String id);
    StudentAcademicsResponse getOngoingAcademicsByStudent(String studentId);
}
