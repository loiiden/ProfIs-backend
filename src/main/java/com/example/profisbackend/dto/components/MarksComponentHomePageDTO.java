package com.example.profisbackend.dto.components;

import com.example.profisbackend.derivedStructures.ScientificWorkMarkDistribution;

//averageMarkForAllScientificWorks can be null in case no work is reviewed with any score
public record MarksComponentHomePageDTO(
        Integer countArchivedScientificWorks,
        Integer countsOpenScientificWorks,
        Double averageMarkForAllScientificWorks,
        ScientificWorkMarkDistribution distribution
) {
}
