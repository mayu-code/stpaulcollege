package com.main.stpaul.dto.response;

import lombok.Data;

@Data
public class CollegeFeesResponse {
    private long id;
    private String stdClass;
    private double totalFees;
    private int installmentGap;
    private double installmentsAmount; 
}