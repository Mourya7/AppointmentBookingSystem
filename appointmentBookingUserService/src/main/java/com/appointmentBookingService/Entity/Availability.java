package com.appointmentBookingService.Entity;

/**
 * Created by mourya on 8/21/18.
 */
public class Availability {
    private Integer availabilityID;
    private String facultyID;
    private Integer termID;
    private String termName;
    private int hasOfficeHours;

    public Availability() {}

    public Integer getAvailabilityID() {
        return availabilityID;
    }

    public void setAvailabilityID(Integer availabilityID) {
        this.availabilityID = availabilityID;
    }

    public String getFacultyID() {
        return facultyID;
    }

    public void setFacultyID(String facultyID) {
        this.facultyID = facultyID;
    }

    public Integer getTermID() {
        return termID;
    }

    public void setTermID(Integer termID) {
        this.termID = termID;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public int getHasOfficeHours() {
        return hasOfficeHours;
    }

    public void setHasOfficeHours(int hasOfficeHours) {
        this.hasOfficeHours = hasOfficeHours;
    }
}
