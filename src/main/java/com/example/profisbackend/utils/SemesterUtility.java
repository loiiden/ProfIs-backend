package com.example.profisbackend.utils;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SemesterUtility {
    public static String getSemesterByDate(LocalDate localDate) {
        int year = localDate.getYear();

        if (localDate == null) {
            return null;
        }
        String season;
        if (localDate.getMonthValue() >= 4 && localDate.getMonthValue() <= 9) {
            season = "SS";
        }else {
            season = "WS";
        }

        if (localDate.getMonthValue() <= 3) {
            //it needs to be decreased due to the exception that timeframe from 01.01.2027 to 31.03.2027 is a part of WS2026 not 2027!!
            //It's just standard semester naming rule in germany.

            year = year - 1;
        }

        String semester = season + year;
        return semester;
    }

    public static String increaseSemester(String semester) {
        if (semester == null || semester.isEmpty()) {
            return null;
        }
        String season = semester.substring(0, 2);
        String year = semester.substring(2);
        int newYear = Integer.parseInt(year);
        String newSeason;
        if (season.equals("SS")) {
            newSeason = "WS";
        }else{
            newSeason = "SS";
            newYear++;
        }
        String newSemester = newSeason + newYear;
        return newSemester;
    }


    public static String decreaseSemester(String semester) {
        if (semester == null || semester.isEmpty()) {
            return null;
        }
        String season = semester.substring(0, 2);
        String year = semester.substring(2);
        int newYear = Integer.parseInt(year);
        String newSeason;
        if (season.equals("SS")) {
            newSeason = "WS";
            newYear--;
        }else{
            newSeason = "SS";
        }
        String newSemester = newSeason + newYear;
        return newSemester;
    }


    public static List<String> getLastNSemestersFromLocalDate(int n, LocalDate localDate) {
        List<String> lastNSemesters = new ArrayList<>();
        String curSemester = getSemesterByDate(localDate);
        for (int i = 0; i < n; i++) {
            lastNSemesters.add(curSemester);
            curSemester = decreaseSemester(curSemester);
        }
        return lastNSemesters;
    }


    public static List<String> getAllSemestersBetweenTwoDates(LocalDate startDate, LocalDate endDate) throws IllegalArgumentException {
        if (startDate == null && endDate == null) {
            throw new IllegalArgumentException("Start date end dates cannot be null at the same time");
        }
        List<String> semesters = new ArrayList<>();
        if (startDate != null && endDate != null) {
            String currentSemester = getSemesterByDate(startDate);

            assert currentSemester != null;

            String endSemester = getSemesterByDate(endDate);
            if (currentSemester.equals(endSemester)) {
                return List.of(currentSemester);
            }else{
                while(!currentSemester.equals(endSemester)) {
                    semesters.add(currentSemester);
                    currentSemester = increaseSemester(currentSemester);
                }
                semesters.add(endSemester);
                return semesters;
            }
        }else{
            if (startDate != null){
                semesters.add(getSemesterByDate(startDate));
            }
            if (endDate != null){
                semesters.add(getSemesterByDate(endDate));
            }
        }
        return semesters;
    }
}
