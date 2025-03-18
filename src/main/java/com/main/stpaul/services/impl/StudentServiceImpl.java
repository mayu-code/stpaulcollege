package com.main.stpaul.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.stpaul.entities.Student;
import com.main.stpaul.repository.StudentRepo;
import com.main.stpaul.services.serviceInterface.StudentService;


@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentRepo studentRepo;

    @Override
    public Student addStudent(Student student) {
        String id = UUID.randomUUID().toString();
        student.setStudentId(id);
        return this.studentRepo.save(student);
    }

    @Override
    public Student getStudentById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStudentById'");
    }

    @Override
    public List<Student> getAllStudents() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllStudents'");
    }
    
}
