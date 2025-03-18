package com.main.stpaul.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class AdmissionForm {

    @Id
    private String admissionId;
    private String admissionDate;
    private String session;
    private String formNo;
    private String stdClass;
    
    private boolean isActive;
    private boolean isDelete;
    
    private LocalDateTime addedDate=LocalDateTime.now();
    private LocalDateTime updateDate=LocalDateTime.now();
    private LocalDateTime deleteDate;
}
