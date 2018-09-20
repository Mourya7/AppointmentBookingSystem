package com.appointmentBookingService.Dao;

import com.appointmentBookingService.Entity.Availability;
import com.appointmentBookingService.Entity.OfficeHours;
import com.appointmentBookingService.Entity.Term;

public interface AdminDAO {
    Term createTerm(String startDate, String endDate, String name);
    void updateTerm(String startDate, String endDate, String name);
    Availability addAvailability(String facultyID, String termName, int hasOfficeHours);
    void updateAvailability(String facultyID,String termName, int hasOfficeHours);
    OfficeHours addOfficeHours(String facultyID, String term, String day, String startTime, String endTime);
    void updateOfficeHours(String facultyID, String term,String day, String startTime, String endTime);
}