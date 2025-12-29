package com.example.profisbackend.entities;

import com.example.profisbackend.enums.AcademicLevel;
import com.example.profisbackend.enums.Salutation;
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
    // @Enumerated(EnumType.STRING) currently we enamurate with numbers, maybe we
    // change it later
    private Salutation salutation;
    private AcademicLevel academicLevel;
}
