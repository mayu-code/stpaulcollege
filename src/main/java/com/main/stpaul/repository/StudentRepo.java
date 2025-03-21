package com.main.stpaul.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.main.stpaul.dto.response.StudentResponse;
import com.main.stpaul.entities.Student;

public interface StudentRepo extends JpaRepository<Student,String>{
    
    @Query("""
        SELECT new com.main.stpaul.dto.response.StudentResponse(
        s.studentId, s.firstName, s.fatherName, s.motherName, s.surname, s.email, s.phoneNo, s.dateOfBirth, s.gender, s.image,
        s.adharNo, s.bloodGroup, s.caste, s.category, s.scholarshipCategory,
        s.admissionDate, s.session, s.stdClass, s.status) 
        FROM Student s WHERE s.isDelete = false
        ORDER BY s.addDate DESC
       """)
    List<StudentResponse> findAllStudents();


    @Query("""
        SELECT new com.main.stpaul.dto.response.StudentResponse(
        s.studentId, s.firstName, s.fatherName, s.motherName, s.surname, s.email, s.phoneNo, s.dateOfBirth, s.gender, s.image,
        s.adharNo, s.bloodGroup, s.caste, s.category, s.scholarshipCategory,
        s.admissionDate, s.session, s.stdClass, s.status) 
        FROM Student s WHERE s.studentId=:id
        AND s.isDelete = false
        ORDER BY s.addDate DESC
       """)
    Optional<StudentResponse> findByStudentId(@Param("id")String id);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Student s WHERE s.email = :email AND s.isDelete = false")
    boolean existsByEmail(@Param("email") String email);

    @Modifying
    @Query("UPDATE Student s SET s.deleteDate=now , s.isDelete=true WHERE s.studentId=:id")
    void deleteStudent(String id);

}
