package com.main.stpaul.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.main.stpaul.entities.Address;
import com.main.stpaul.entities.Documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDetailResponse {
    private String studentId;

    private String firstName;
    private String fatherName;
    private String motherName;
    private String surname;
    private String email;
    private String phoneNo;
    private LocalDate dateOfBirth;
    private String gender;
    private byte[] image;

    private String adharNo;
    private String bloodGroup;
    private String caste;
    private String category;
    private String scholarshipCategory;

    private LocalDate admissionDate;
    private String session;
    private String stdClass;
    private String status;

    private List<StudentAcademicsResponse> studentAcademics;

    private List<Address> addresses;

    private List<DocumentReponse> documents;
    
    private GuardianInfoResponse guardianInfo;

    private BankDetailResponse bankDetail;

    private LastSchoolResponse lastSchool;
}
