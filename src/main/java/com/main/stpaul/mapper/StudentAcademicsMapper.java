package com.main.stpaul.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.main.stpaul.dto.response.StudentAcademicsResponse;
import com.main.stpaul.entities.StudentAcademics;

@Component
public class StudentAcademicsMapper {
    
    @Autowired
    private ModelMapper modelMapper;

    public StudentAcademics toStudentAcademics(StudentAcademicsResponse studentAcademicsResponse){
        return this.modelMapper.map(studentAcademicsResponse, StudentAcademics.class);
    }
}
