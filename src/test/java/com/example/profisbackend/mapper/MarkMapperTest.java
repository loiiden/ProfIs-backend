package com.example.profisbackend.mapper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MarkMapperTest {
    @Test
    void testPointsToGermanNote(){
        assertEquals("1,0", MarkMapper.pointsToGermanNote(96F));
        assertEquals("1,1", MarkMapper.pointsToGermanNote(94F));
        assertEquals("1,4", MarkMapper.pointsToGermanNote(89F));
        assertEquals("1,7", MarkMapper.pointsToGermanNote(85F));
        assertEquals("2,2", MarkMapper.pointsToGermanNote(77F));
        assertEquals("3,5", MarkMapper.pointsToGermanNote(58F));
        assertEquals("3,7", MarkMapper.pointsToGermanNote(54.5F));
        assertEquals("4,0", MarkMapper.pointsToGermanNote(50F));
        assertEquals("5,0", MarkMapper.pointsToGermanNote(49.5F));
        assertEquals("5,0", MarkMapper.pointsToGermanNote(35.5F));
        assertThrows(IllegalArgumentException.class, ()->{
            MarkMapper.pointsToGermanNote(105F);
        });
    }
}
