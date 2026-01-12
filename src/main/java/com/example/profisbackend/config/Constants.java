package com.example.profisbackend.config;

public class Constants {
    public static int SCIENTIFIC_WORK_SHEET = 0;
    public static final int STUDY_PROGRAM_SHEET = 1;

    // Study Program Sheet
    public static final int DEGREE_COLUMN = 0;
    public static final int STUDY_PROGRAM_TITLE_COLUMN = 1;
    public static final int SEMESTER_WEEKS_HOURS_COLUMN = 2;

    // --- SCIENTIFIC WORK SHEET COLUMNS ---

    // Erstprüfer (Starts at Index 1)
    public static final int FIRST_EVALUATOR_SALUTATION = 1;    // Spalte B (Index 1)
    public static final int FIRST_EVALUATOR_FIRSTNAME = 2;     // Spalte C
    public static final int FIRST_EVALUATOR_LASTNAME = 3;      // Spalte D
    public static final int FIRST_EVALUATOR_ACADEMICLEVEL = 4; // Spalte E
    public static final int FIRST_EVALUATOR_ROLE = 5;          // Spalte F
    public static final int FIRST_EVALUATOR_ADDRESS = 6;       // Spalte G
    public static final int FIRST_EVALUATOR_EMAIL = 7;         // Spalte H
    public static final int FIRST_EVALUATOR_PHONE = 8;         // Spalte I

    // Zweitprüfer (Starts at Index 9)
    public static final int SECOND_EVALUATOR_SALUTATION = 9;   // Spalte J (Index 9)
    public static final int SECOND_EVALUATOR_FIRSTNAME = 10;   // Spalte K
    public static final int SECOND_EVALUATOR_LASTNAME = 11;    // Spalte L
    public static final int SECOND_EVALUATOR_ACADEMICLEVEL = 12;// Spalte M
    public static final int SECOND_EVALUATOR_ROLE = 13;        // Spalte N
    public static final int SECOND_EVALUATOR_ADDRESS = 14;     // Spalte O
    public static final int SECOND_EVALUATOR_EMAIL = 15;       // Spalte P
    public static final int SECOND_EVALUATOR_PHONE = 16;       // Spalte Q

    // Student (Shifted)
    public static final int STUDENT_FIRSTNAME_COLUMN = 17;
    public static final int STUDENT_LASTNAME_COLUMN = 18;
    public static final int STUDENT_ACADEMICLEVEL_COLUMN = 19;
    public static final int STUDENT_SALUTATION_COLUMN = 20;
    public static final int STUDENT_PHONENUMBER_COLUMN = 21;
    public static final int STUDENT_MAIL_COLUMN = 22;
    public static final int STUDENT_NUMBER_COLUMN = 23;
    public static final int STUDENT_ADDRESS_COLUMN = 24;

    // Studyprogram (Shifted)
    public static final int STUDYPROGRAM_DEGREETYPE_COLUMN = 25;
    public static final int STUDYPROGRAM_TITLE = 26;

    // Work Details (Shifted)
    public static final int WORK_TITLE_COLUMN = 27;
    public static final int START_DATE_COLUMN = 28;
    public static final int END_DATE_COLUMN = 29;
    public static final int COLLOQUIUM_DATE_COLUMN = 30;
    public static final int COLLOQUIUM_LOCATION_COLUMN = 31;
    public static final int PRESENTATION_START_COLUMN = 32;
    public static final int PRESENTATION_END_COLUMN = 33;
    public static final int DISCUSSION_START_COLUMN = 34;
    public static final int DISCUSSION_END_COLUMN = 35;

    // Marks
    public static final int MARK_MAIN_WORK = 36;
    public static final int MARK_MAIN_COLLOQ = 37;
    public static final int MARK_SECOND_WORK = 38;
    public static final int MARK_SECOND_COLLOQ = 39;

    public static final int COMMENT_COLUMN = 41; // Assuming it stays two steps after the last mark

    //Events
    public static final int EVENT_PLANNED_COLUMN = 42;
    public static final int EVENT_PROPOSAL_COLUMN = 43;
    public static final int EVENT_DISCUSSION_COLUMN = 44;
    public static final int EVENT_FINAL_SUBMISSION_COLUMN = 45;
    public static final int EVENT_REVIEW_COLUMN = 46;
    public static final int EVENT_ARCHIVE_COLUMN = 47;
    public static final int EVENT_ABORT_COLUMN = 48;
}