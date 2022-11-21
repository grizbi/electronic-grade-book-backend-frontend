package com.example.electronicgradebook.dto;

import com.example.electronicgradebook.resources.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@Getter
@Setter
@NoArgsConstructor
public class SpecialStudentsDto {

    private User highestAverageGradeStudent;
    private User lowestAverageGradeStudent;
    private String mostOftenObtainedMark;
    private String leastFrequentlyObtainedMark;
}
