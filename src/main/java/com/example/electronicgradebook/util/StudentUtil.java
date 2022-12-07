package com.example.electronicgradebook.util;

import com.example.electronicgradebook.dto.SpecialStudentsDto;
import com.example.electronicgradebook.resources.User;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentUtil {

    private StudentUtil() {

    }

    public static Integer getTotalOfMarks(List<User> students) {
        students = getEligibleStudentsForAverageGradeCalculation(students);
        int total = 0;
        for(User student : students) {
            String[] marks = student.getMarks().split(",");
            total += marks.length;
        }

        return total;
    }

    public static List<User> getAllStudents(List<User> students) {
        return students.stream().filter(student -> !student.getEmail().equals("admin")).collect(Collectors.toList());
    }

    public static void overrideUserWithNewData(User userToBeUpdated, User newData) {
        userToBeUpdated.setMarks(newData.getMarks());
        userToBeUpdated.setEmail(newData.getEmail());
        userToBeUpdated.setName(newData.getName());
        userToBeUpdated.setSurname(newData.getSurname());
    }

    public static List<String> getMostOftenAndLeastFrequentlyObtainedMarks(List<User> students) {
        students = getEligibleStudentsForAverageGradeCalculation(students);
        String mostFrequentlyObtainedMark = "";
        String leastFrequentlyObtainedMark = "";
        Map<String, Integer> gradeOccurrences = new HashMap<>();
        String[] gradesForCurrentStudent;
        for (User student : students) {
            gradesForCurrentStudent = student.getMarks().split(",");
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
        students = getEligibleStudentsForAverageGradeCalculation(students);
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
        return students.stream().filter(student -> student.getMarks() != null && !student.getMarks().isBlank())
                .collect(Collectors.toList());
    }

    public static double getAverageGradeForStudent(User user) {
        if(user.getMarks() == null || user.getMarks().isEmpty()) {
            return 0.0;
        }

        String[] marks = user.getMarks().split(",");
        double averageStudentGrade = 0.0;
        for (String mark : marks) {
            averageStudentGrade += Double.parseDouble(mark);
        }
        return trimNumberTo2Decimals(averageStudentGrade / marks.length);
    }

    public static double trimNumberTo2Decimals(Double number) {
        return Double.parseDouble(new DecimalFormat("#.##").format(number));
    }
}
