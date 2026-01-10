package com.example.profisbackend.mapper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MarkMapperTest {
    @Test
    void testPointsToGermanNote(){
        assertEquals("1,0", MarkMapper.pointsToGermanNoteString(96));
        assertEquals("1,1", MarkMapper.pointsToGermanNoteString(94));
        assertEquals("1,4", MarkMapper.pointsToGermanNoteString(89));
        assertEquals("1,7", MarkMapper.pointsToGermanNoteString(85));
        assertEquals("2,2", MarkMapper.pointsToGermanNoteString(77));
        assertEquals("3,5", MarkMapper.pointsToGermanNoteString(58));
        assertEquals("3,7", MarkMapper.pointsToGermanNoteString(54.5));
        assertEquals("4,0", MarkMapper.pointsToGermanNoteString(50));
        assertEquals("5,0", MarkMapper.pointsToGermanNoteString(49.5));
        assertEquals("5,0", MarkMapper.pointsToGermanNoteString(35.5));
        assertThrows(IllegalArgumentException.class, ()->{
            MarkMapper.pointsToGermanNote(105F);
        });
    }
}
