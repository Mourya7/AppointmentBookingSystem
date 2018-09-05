package com.appointmentBookingService.Entity;

/**
 * Created by mourya on 8/22/18.
 */
public class OfficeHours {
    private String id;
    private String availabilityID;
    private String day;
    private String startTime;
    private String endTime;

    public OfficeHours() {};

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvailabilityID() {
        return availabilityID;
    }

    public void setAvailabilityID(String availabilityID) {
        this.availabilityID = availabilityID;
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
