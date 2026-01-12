package com.example.profisbackend.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LocalDateUtility {
    public static List<LocalDate> parseEventDatesStringToLocalDates(String eventDates){
        List<LocalDate> eventDatesList = new ArrayList<>();
        String[] eventDatesString = eventDates.split(";");
        for(int i = 0; i < eventDatesString.length; i++){
            LocalDate localDate = LocalDate.parse(eventDatesString[i]);
            eventDatesList.add(localDate);
        }
        return eventDatesList;
    }
}
