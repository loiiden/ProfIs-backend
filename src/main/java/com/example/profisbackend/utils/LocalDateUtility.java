package com.example.profisbackend.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LocalDateUtility {
    public static List<LocalDate> parseEventDatesStringToLocalDates(String eventDates){
        List<LocalDate> eventDatesList = new ArrayList<>();
        String[] eventDatesString = eventDates.split(";");
        for(int i = 0; i < eventDatesString.length; i++){
            if (!eventDatesString[i].isEmpty()){
                try {
                    LocalDate localDate = LocalDate.parse(eventDatesString[i]);
                    eventDatesList.add(localDate);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        return eventDatesList;
    }
}
