package com.main.stpaul.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdmissionFormRequest {
    private String admissionDate;
    private String session;
    private String formNo;
    private String stdClass;
}
