package com.main.stpaul.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class StudentAcademics {

    @Id
    private String studentAcademicsId;

    private String collegeName;
    private String lastStudentId;
    private String rollNo;
    private String uid;
    private String examination;
    private String examMonth;
    private int marksObtained;
    private String result;
    private boolean isAlumni;
    private LocalDate promotionDate;
    private String status;

    private boolean isDelete = false;
    private boolean isActive = true;
    
    private LocalDateTime addDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();
    private LocalDateTime deleteDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;


    @OneToOne(mappedBy = "studentAcademics")
    private PaymentDetail paymentDetail;
}