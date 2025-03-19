package com.main.stpaul.services.serviceInterface;

import java.util.List;
import com.main.stpaul.entities.CollegeFees;

public interface CollegeFeesService {
    CollegeFees addCollegeFees(CollegeFees collegeFees);
    List<CollegeFees> getAllCollegeFees();
    CollegeFees getCollegeFees(long id);
    void updateCollegeFees(CollegeFees collegeFees);
    void deleteCollegeFees(long id);
}
