package com.appointmentBookingService.Entity;

import java.sql.Time;
import java.util.Date;

/**
 * Created by mourya on 8/21/18.
 */
public class Appointment {
    private String meetingID;
    private String studentID;
    private String facultyID;
    private String startTime;
    private String endTime;
    private String officeHoursID;
    private String description;
    private String accepted;
    private String date;

    public Appointment() {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMeetingID() {
        return meetingID;
    }

    public void setMeetingID(String meetingID) {
        this.meetingID = meetingID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getFacultyID() {
        return facultyID;
    }

    public void setFacultyID(String facultyID) {
        this.facultyID = facultyID;
    }

    public String getOfficeHoursID() {
        return officeHoursID;
    }

    public void setOfficeHoursID(String officeHoursID) {
        this.officeHoursID = officeHoursID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }
}
