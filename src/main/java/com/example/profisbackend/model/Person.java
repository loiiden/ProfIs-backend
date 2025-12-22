package com.example.profisbackend.model;
import com.example.profisbackend.enums.AcademicLevel;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class Person {
    
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String phoneNumber;
    private AcademicLevel academicLevel;

    //@Lob - was deleted due to SQL-lite driver issues.
    @Column(columnDefinition = "BLOB")
    private byte[] profilePicture;
}
