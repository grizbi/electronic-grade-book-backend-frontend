package com.example.electronicgradebook.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentAverageGradeDto {

    private String name;
    private String surname;
    private double averageGrade;

}
