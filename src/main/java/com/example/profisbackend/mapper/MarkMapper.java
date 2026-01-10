package com.example.profisbackend.mapper;

public class MarkMapper {
    public static Double pointsToGermanNote(double points) throws IllegalArgumentException {
        if (points < 0 || points > 100) {
            throw new IllegalArgumentException();
        }
        if (points >= 95) {
            return 1.0;
        }
        if (points < 50) {
            return 5.0;
        }
        double mark = 1.0F;
        double n = 95F;
        while (points < n) {
            n = (double) (n - 1.5);
            mark = (double) (mark + 0.1);
        }
        return mark;
    }
    public static String pointsToGermanNoteString(double points) throws IllegalArgumentException{
        Double mark = pointsToGermanNote(points);
        return String.format("%.1f", mark).replace('.', ',');
    }
}
