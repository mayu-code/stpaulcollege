package com.main.stpaul.entities;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Stream {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stream;
    private  String subStream;
    private String medium;


    private boolean isDelete = false;
    private boolean isActive = true;

    private LocalDateTime addDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();
    private LocalDateTime deleteDate;

    @OneToMany(mappedBy = "stream")
    private List<Subject> subjects;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "student_academics_id")
    private StudentAcademics academics;
}
