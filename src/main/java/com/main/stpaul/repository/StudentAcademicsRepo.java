package com.main.stpaul.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.main.stpaul.constants.Status;
import com.main.stpaul.dto.response.StudentAcademicsResponse;
import com.main.stpaul.entities.StudentAcademics;

import jakarta.transaction.Transactional;

public interface StudentAcademicsRepo extends JpaRepository<StudentAcademics,String> {
    
    
    @Query("SELECT s FROM StudentAcademics s WHERE s.student.id=:id AND s.isDelete=false")
    List<StudentAcademics> findByStudent(long id);

    @Query("SELECT s FROM StudentAcademics s WHERE s.studentAcademicsId=:id AND s.isDelete=false")
    Optional<StudentAcademics> findById(long id);

    @Transactional
    @Modifying
    @Query("UPDATE StudentAcademics s SET s.isDelete=true AND s.deleteDate=now WHERE s.studentAcademicsId=:id")
    void deleteStudentAcademics(long id);

    @Query("""
        SELECT new com.main.stpaul.dto.response.StudentAcademicsResponse(
            s.studentAcademicsId, s.collegeName, s.rollNo, s.examination, s.examMonth, 
            s.marksObtained, s.stdClass, s.result, s.isAlumni, s.promotionDate, s.status,null,
            s.stream,
            s.biofocalSubject
        ) 
        FROM StudentAcademics s
        WHERE s.student.id = :studentId AND s.isDelete = false
        ORDER BY s.addDate DESC
    """)
    List<StudentAcademicsResponse> findByStudentId(@Param("studentId") String studentId);

    @Query("""
        SELECT new com.main.stpaul.dto.response.StudentAcademicsResponse(
            s.studentAcademicsId, s.collegeName, s.rollNo, s.examination, s.examMonth, 
            s.marksObtained, s.stdClass, s.result, s.isAlumni, s.promotionDate, s.status,null,
            s.stream,
            s.biofocalSubject
        ) 
        FROM StudentAcademics s
        WHERE s.studentAcademicsId = :id AND s.isDelete = false
    """)
    Optional<StudentAcademicsResponse> findAcademicsById(String id);

    @Query("""
        SELECT new com.main.stpaul.dto.response.StudentAcademicsResponse(
            s.studentAcademicsId, s.collegeName, s.rollNo, s.examination, s.examMonth, 
            s.marksObtained, s.stdClass, s.result, s.isAlumni, s.promotionDate, s.status,null,
            s.stream,
            s.biofocalSubject
        ) 
        FROM StudentAcademics s
        WHERE s.student.id = :studentId AND s.isDelete = false AND s.status=:status
    """)
    Optional<StudentAcademicsResponse> findOngoingAcademicsByStudent(@Param("studentId") String studentId,@Param("status")Status status);
}
