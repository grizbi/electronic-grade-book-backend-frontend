package com.example.electronicgradebook.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentDto {
    private String name;
    private String surname;
    private String marks;
}
