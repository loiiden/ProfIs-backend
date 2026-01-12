package com.example.profisbackend.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class LocalDateUtilityTest {
    @Test
    void testParseEventDatesStringToLocalDates(){
        String eventDatesString = "2020-01-01;2021-01-01;";
        assertEquals(LocalDateUtility.parseEventDatesStringToLocalDates(eventDatesString), List.of(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1)));
        String emptyString = "";
        assertEquals(LocalDateUtility.parseEventDatesStringToLocalDates(emptyString), List.of());
    }
}
