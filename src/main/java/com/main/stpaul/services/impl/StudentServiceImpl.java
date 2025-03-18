package com.main.stpaul.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.stpaul.dto.response.StudentDetailResponse;
import com.main.stpaul.dto.response.StudentResponse;
import com.main.stpaul.entities.Student;
import com.main.stpaul.mapper.StudentMapper;
import com.main.stpaul.repository.StudentRepo;
import com.main.stpaul.services.serviceInterface.StudentService;


@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Student addStudent(Student student) {
        String id = UUID.randomUUID().toString();
        student.setStudentId(id);
        return this.studentRepo.save(student);
    }

    @Override
    public StudentDetailResponse getStudentById(String id) {
        StudentResponse response = this.studentRepo.findByStudentId(id).orElse(null);
        if(response==null){
            return null;
        }
        StudentDetailResponse student = this.studentMapper.toStudentDetailResponse(response);
        return student;
    }

    @Override
    public List<StudentResponse> getAllStudents() {
        return this.studentRepo.findAllStudents();
    }
    
}
