package com.example.profisbackend.utils;

import com.example.profisbackend.mapper.MarkMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SemesterUtilityTest {
    @Test
    void testGetSemesterByDate(){
        assertEquals("SS2024", SemesterUtility.getSemesterByDate(LocalDate.of(2024, 5, 1)));
        assertEquals("WS2022", SemesterUtility.getSemesterByDate(LocalDate.of(2023, 1, 1)));
        assertEquals("SS2026", SemesterUtility.getSemesterByDate(LocalDate.of(2026, 9, 30)));
        assertEquals("WS2025", SemesterUtility.getSemesterByDate(LocalDate.of(2026, 2, 28)));
    }
    @Test
    void testIncreaseSemester(){
        assertEquals("WS2024", SemesterUtility.increaseSemester("SS2024"));
        assertEquals("SS2025", SemesterUtility.increaseSemester("WS2024"));
    }
    @Test
    void testGetAllSemestersBetweenTwoDates(){
        assertEquals(
                List.of("WS2025"), SemesterUtility.getAllSemestersBetweenTwoDates(
                        LocalDate.of(2025, 10, 1),
                        LocalDate.of(2026, 1, 28)
                )
        );

        assertEquals(
                List.of("WS2025", "SS2026"), SemesterUtility.getAllSemestersBetweenTwoDates(
                        LocalDate.of(2025, 10, 1),
                        LocalDate.of(2026, 9, 28)
                )
        );

        assertEquals(
                List.of("WS2025", "SS2026", "WS2026"), SemesterUtility.getAllSemestersBetweenTwoDates(
                        LocalDate.of(2025, 10, 1),
                        LocalDate.of(2027, 1, 28)
                )
        );

        assertEquals(
                List.of("WS2025", "SS2026", "WS2026"), SemesterUtility.getAllSemestersBetweenTwoDates(
                        LocalDate.of(2025, 10, 1),
                        LocalDate.of(2026, 10, 1)
                )
        );
    }
    @Test
    void getLastNSemestersFromLocalDate(){
        assertEquals(SemesterUtility.getLastNSemestersFromLocalDate(5, LocalDate.of(2026, 1, 11)), List.of("WS2025", "SS2025", "WS2024", "SS2024", "WS2023"));
    }
}
