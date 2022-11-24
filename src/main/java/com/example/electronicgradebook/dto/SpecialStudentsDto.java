package com.example.electronicgradebook.dto;

import com.example.electronicgradebook.resources.User;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialStudentsDto {

    private User highestAverageGradeStudent;
    private User lowestAverageGradeStudent;
    private String mostOftenObtainedMark;
    private String leastFrequentlyObtainedMark;
}
