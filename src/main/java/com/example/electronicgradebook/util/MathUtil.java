package com.example.electronicgradebook.util;

import com.example.electronicgradebook.resources.User;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

public class MathUtil {
    private MathUtil() {

    }

    public static double getAverageGradeForClass(List<User> students) {
        List<User> eligibleStudentsForAverageGradeCalculation = students.stream()
                .filter(student -> student.getGrades() != null).collect(Collectors.toList());
        double averageClassGrade = 0.0;
        for (User user : eligibleStudentsForAverageGradeCalculation) {
            averageClassGrade += getAverageGradeForStudent(user);
        }
        return trimNumberTo2Decimals(averageClassGrade / eligibleStudentsForAverageGradeCalculation.size());
    }

    public static double getAverageGradeForStudent(User user) {
        String[] marks = user.getGrades().split(",");
        double averageStudentGrade = 0.0;
        for (String mark : marks) {
            averageStudentGrade += Double.parseDouble(mark);
        }
        return averageStudentGrade / marks.length;
    }

    public static double trimNumberTo2Decimals(Double number) {
        return Double.parseDouble(new DecimalFormat("#.##").format(number));
    }
}
