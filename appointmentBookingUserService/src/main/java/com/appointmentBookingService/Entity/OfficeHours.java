package com.appointmentBookingService.Entity;

/**
 * Created by mourya on 8/22/18.
 */
public class OfficeHours {
    private Integer officeHoursID;
    private Integer availabilityID;
    private String day;
    private String startTime;
    private String endTime;
    private String facultyID;
    private String termName;

    public Integer getAvailabilityID() {
        return availabilityID;
    }

    public void setAvailabilityID(Integer availabilityID) {
        this.availabilityID = availabilityID;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public String getFacultyID() {
        return facultyID;
    }

    public void setFacultyID(String facultyID) {
        this.facultyID = facultyID;
    }

    public OfficeHours() {};

    public Integer getOfficeHoursID() {
        return officeHoursID;
    }

    public void setOfficeHoursID(Integer officeHoursID) {
        this.officeHoursID = officeHoursID;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
