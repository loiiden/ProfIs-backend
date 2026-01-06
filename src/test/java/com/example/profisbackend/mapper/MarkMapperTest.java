package com.example.profisbackend.mapper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MarkMapperTest {
    @Test
    void testPointsToGermanNote(){
        assertEquals("1,0", MarkMapper.pointsToGermanNote(96));
        assertEquals("1,1", MarkMapper.pointsToGermanNote(94));
        assertEquals("1,4", MarkMapper.pointsToGermanNote(89));
        assertEquals("1,7", MarkMapper.pointsToGermanNote(85));
        assertEquals("2,2", MarkMapper.pointsToGermanNote(77));
        assertEquals("3,5", MarkMapper.pointsToGermanNote(58));
        assertEquals("3,7", MarkMapper.pointsToGermanNote(54.5));
        assertEquals("4,0", MarkMapper.pointsToGermanNote(50));
        assertEquals("5,0", MarkMapper.pointsToGermanNote(49.5));
        assertEquals("5,0", MarkMapper.pointsToGermanNote(35.5));
        assertThrows(IllegalArgumentException.class, ()->{
            MarkMapper.pointsToGermanNote(105F);
        });
    }
}
