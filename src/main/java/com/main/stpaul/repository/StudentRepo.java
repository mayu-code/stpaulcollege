package com.main.stpaul.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.main.stpaul.constants.Status;
import com.main.stpaul.dto.response.PendingStudents;
import com.main.stpaul.entities.Student;

public interface StudentRepo extends JpaRepository<Student,String>{
    
    @Query("""
        SELECT s 
        FROM Student s WHERE s.isDelete = false AND s.studentAcademics Is Not Null
        ORDER BY s.addDate DESC
       """)
    List<Student> findAllStudents();


    @Query("""
        SELECT s 
        FROM Student s WHERE s.studentId=:id
        AND s.isDelete = false
        ORDER BY s.addDate DESC
       """)
    Optional<Student> findByStudentId(@Param("id")String id);

    @Query("""
            SELECT new com.main.stpaul.dto.response.PendingStudents(s.studentId, s.firstName, s.fatherName, s.surname,
            s.email, s.phoneNo, s.dateOfBirth, s.admissionDate,  sa.studentAcademicsId,
            s.session, s.stdClass, s.status,0.0)
            FROM Student s 
            LEFT JOIN studentAcademics sa
            ON s.studentId=sa.student.studentId AND sa.isDelete = false
            WHERE s.status = :status 
            AND s.isDelete = false
            """)
    List<PendingStudents> findByStatus(Status status);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Student s WHERE s.email = :email AND s.isDelete = false")
    boolean existsByEmail(@Param("email") String email);

    @Modifying
    @Query("UPDATE Student s SET s.deleteDate=now , s.isDelete=true WHERE s.studentId=:id")
    void deleteStudent(String id);

}
