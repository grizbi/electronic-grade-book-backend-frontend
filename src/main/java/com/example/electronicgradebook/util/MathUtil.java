package com.example.electronicgradebook.util;

import com.example.electronicgradebook.dto.SpecialStudentsDto;
import com.example.electronicgradebook.resources.User;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MathUtil {

    private MathUtil() {

    }

    public static List<String> getMostOftenAndLeastFrequentlyObtainedMarks(List<User> students) {
        students = getEligibleStudentsForAverageGradeCalculation(students);
        String mostFrequentlyObtainedMark = "";
        String leastFrequentlyObtainedMark = "";
        Map<String, Integer> gradeOccurrences = new HashMap<>();
        String[] gradesForCurrentStudent;
        for (User student : students) {
            gradesForCurrentStudent = student.getGrades().split(",");
            for (String s : gradesForCurrentStudent) {
                if (gradeOccurrences.containsKey(s)) {
                    gradeOccurrences.put(s, gradeOccurrences.get(s) + 1);
                } else {
                    gradeOccurrences.put(s, 1);
                }
            }
        }

        int maxOccurrencesOfMark = 0;
        int leastOccurrencesOfMark = 1000;

        for (String mark : gradeOccurrences.keySet()) {
            if (gradeOccurrences.get(mark) > maxOccurrencesOfMark) {
                maxOccurrencesOfMark = gradeOccurrences.get(mark);
                mostFrequentlyObtainedMark = mark;
            }
            if (gradeOccurrences.get(mark) < leastOccurrencesOfMark) {
                leastOccurrencesOfMark = gradeOccurrences.get(mark);
                leastFrequentlyObtainedMark = mark;
            }
        }
        return Arrays.asList(leastFrequentlyObtainedMark, mostFrequentlyObtainedMark);
    }


    public static SpecialStudentsDto getSpecialStudents(List<User> students) {
        students = getEligibleStudentsForAverageGradeCalculation(students);
        return SpecialStudentsDto.builder()
                .highestAverageGradeStudent(getStudentWithHighestAverageGrade(students))
                .lowestAverageGradeStudent(getStudentWithLowestAverageGrade(students))
                .leastFrequentlyObtainedMark(getMostOftenAndLeastFrequentlyObtainedMarks(students).get(0))
                .mostOftenObtainedMark(getMostOftenAndLeastFrequentlyObtainedMarks(students).get(1))
                .build();
    }

    public static User getStudentWithLowestAverageGrade(List<User> students) {
        User tempStudent = new User();
        double maxGrade = 6.0;
        for (User student : students) {
            if (getAverageGradeForStudent(student) < maxGrade) {
                maxGrade = getAverageGradeForStudent(student);
                tempStudent = student;
            }
        }

        return tempStudent;
    }

    public static User getStudentWithHighestAverageGrade(List<User> students) {
        User tempStudent = new User();
        double maxGrade = 0.0;
        for (User student : students) {
            if (getAverageGradeForStudent(student) > maxGrade) {
                maxGrade = getAverageGradeForStudent(student);
                tempStudent = student;
            }
        }

        return tempStudent;
    }

    public static double getAverageGradeForClass(List<User> students) {
        List<User> eligibleStudentsForAverageGradeCalculation = getEligibleStudentsForAverageGradeCalculation(students);
        double averageClassGrade = 0.0;
        for (User user : eligibleStudentsForAverageGradeCalculation) {
            averageClassGrade += getAverageGradeForStudent(user);
        }
        return trimNumberTo2Decimals(averageClassGrade / eligibleStudentsForAverageGradeCalculation.size());
    }

    private static List<User> getEligibleStudentsForAverageGradeCalculation(List<User> students) {
        return students.stream().filter(student -> student.getGrades() != null).collect(Collectors.toList());
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
