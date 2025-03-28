package com.main.stpaul.services.serviceInterface;

import java.util.List;

import com.main.stpaul.dto.response.PendingStudents;
import com.main.stpaul.entities.Student;

public interface StudentService {

    Student addStudent(Student student);
    Student findById(String id);
    Student getStudentById(String id);
    List<Student> getAllStudents(String query,String stdClass,String section,String session);

    Student updateStudent(Student student);
    void deleteStudent(String id);

    Student getData(String id);
    List<PendingStudents> getPendingStudents(String query,String stdClass,String section,String session);
    Student promoteStudent(Student student);

    List<Student> getFailStudents(String query,String stdClass,String section,String session);
    
}