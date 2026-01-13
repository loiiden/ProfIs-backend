package com.example.profisbackend.utils;

import com.example.profisbackend.entities.Evaluator;
import com.example.profisbackend.entities.ScientificWork;
import com.example.profisbackend.entities.Student;
import com.example.profisbackend.enums.AcademicLevel;

public class AcademicLevelUtility {
    public static boolean doesEvaluatorAllowedToMentorStudent(Evaluator evaluator, Student student) {
        assert evaluator != null;
        assert student != null;

        AcademicLevel evaluatorAcademicLevel = evaluator.getAcademicLevel();
        AcademicLevel studentAcademicLevel = student.getAcademicLevel();
        if (evaluatorAcademicLevel == null || studentAcademicLevel == null) {
            return true;
        }
        if (studentAcademicLevel.isHigherThan(evaluatorAcademicLevel)) {
            return false;
        }
        return true;
    }

    public static boolean isConfigurationOfScientificWorkAllowed(ScientificWork scientificWork) {
        boolean mainEvaluatorIsAllowedToMentorStudent = true;
        boolean secondEvaluatorIsAllowedToMentorStudent = true;
        if (scientificWork.getStudent() != null ) {
            if  (scientificWork.getMainEvaluator() != null){
                mainEvaluatorIsAllowedToMentorStudent = AcademicLevelUtility.doesEvaluatorAllowedToMentorStudent(scientificWork.getMainEvaluator(), scientificWork.getStudent());
                if (!mainEvaluatorIsAllowedToMentorStudent){
                    return false;
                }
            }
            if (scientificWork.getSecondEvaluator() != null){
                secondEvaluatorIsAllowedToMentorStudent = AcademicLevelUtility.doesEvaluatorAllowedToMentorStudent(scientificWork.getSecondEvaluator(), scientificWork.getStudent());
                if (!secondEvaluatorIsAllowedToMentorStudent){
                    return false;
                }
            }
        }
        return true;
    }
}
