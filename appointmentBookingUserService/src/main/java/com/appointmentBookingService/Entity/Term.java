package com.appointmentBookingService.Entity;

import java.util.Date;

/**
 * Created by mourya on 8/22/18.
 */
public class Term {
    private Integer termID;
    private String startDate;
    private String endDate;
    private String name;

    public Integer getTermID() {
        return termID;
    }

    public void setTermID(Integer termID) {
        this.termID = termID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}