package com.main.stpaul.dto.request;

import java.time.LocalDate;
import java.util.Date;

import lombok.Data;

@Data
public class StudentRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Date dateOfBirth;
    private String gender;

    private LocalDate admissionDate;
    private String session;
    private String stdClass;
}
