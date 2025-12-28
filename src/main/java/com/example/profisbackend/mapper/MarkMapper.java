package com.example.profisbackend.mapper;

public class MarkMapper {
    public static String pointsToGermanNote(Float points) throws IllegalArgumentException {
       if (points < 0 || points > 100) {
           throw new IllegalArgumentException();
       }
       if (points >= 95) {
           return "1,0";
       }
       if (points < 50){
           return "5,0";
       }
       Float mark = 1.0F;
       Float n = 95F;
       while (points < n){
           n = (float) (n - 1.5);
           mark = (float)(mark + 0.1);
       }
       return String.format("%.1f", mark).replace('.', ',');
    }
}
