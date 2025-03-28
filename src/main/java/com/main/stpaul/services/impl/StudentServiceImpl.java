package com.main.stpaul.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.stpaul.constants.Result;
import com.main.stpaul.constants.Status;
import com.main.stpaul.dto.response.PendingStudents;
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
        Student response = this.studentRepo.findByStudentId(id).orElse(null);
        return response;
    }

    @Override
    public List<Student> getAllStudents(String query,String stdClass,String section,String session) {
        return this.studentRepo.findAllStudents(query,stdClass,section,session);
    }

    @Override
    public Student updateStudent(Student student) {
        return this.studentRepo.save(student);
    }

    @Override
    public void deleteStudent(String id) {
        this.studentRepo.deleteStudent(id);
        return;
    }

    @Override
    public Student findById(String id) {
        return this.studentRepo.findById(id).get();
    }

    @Override
    public List<PendingStudents> getPendingStudents(String query,String stdClass,String section,String session) {
        return this.studentRepo.findByStatus(query,stdClass,section,session,Status.Pending);
    }

    @Override
    public List<Student> getFailStudents(String query,String stdClass,String section,String session) {
        return this.studentRepo.findFailStudents(query,stdClass,section,session,Result.FAIL);
    }
    
}
