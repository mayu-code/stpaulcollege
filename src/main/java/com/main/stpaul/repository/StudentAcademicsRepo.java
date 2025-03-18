package com.main.stpaul.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.main.stpaul.entities.StudentAcademics;

public interface StudentAcademicsRepo extends JpaRepository<StudentAcademics,String> {
    
    
    @Query("SELECT s FROM StudentAcademics s WHERE s.student.id=:id AND s.isDelete=false")
    List<StudentAcademics> findByStudent(long id);

    @Query("SELECT s FROM StudentAcademics s WHERE s.studentAcademicsId=:id AND s.isDelete=false")
    Optional<StudentAcademics> findById(long id);

    @Modifying
    @Query("UPDATE StudentAcademics s SET s.isDelete=true AND s.deleteDate=now WHERE s.studentAcademicsId=:id")
    void deleteStudentAcademics(long id);
}
