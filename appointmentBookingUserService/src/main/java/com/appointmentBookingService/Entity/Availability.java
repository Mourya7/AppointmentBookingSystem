package com.appointmentBookingService.Entity;

/**
 * Created by mourya on 8/21/18.
 */
public class Availability {
    private String id;
    private String facultyID;
    private String term;
    private int hasOfficeHours;

    public Availability() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFacultyID() {
        return facultyID;
    }

    public void setFacultyID(String facultyID) {
        this.facultyID = facultyID;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getHasOfficeHours() {
        return hasOfficeHours;
    }

    public void setHasOfficeHours(int hasOfficeHours) {
        this.hasOfficeHours = hasOfficeHours;
    }
}
