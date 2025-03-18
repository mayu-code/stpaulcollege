package com.main.stpaul.services.serviceInterface;

import java.util.List;

import com.main.stpaul.entities.Student;

public interface StudentService {
    
    Student addStudent(Student student);
    Student getStudentById(String id);
    List<Student> getAllStudents();
}
