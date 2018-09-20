package com.appointmentBookingService.Dao;

import com.appointmentBookingService.Entity.*;

import java.util.Collection;

/**
 * Created by mourya on 8/21/18.
 */
public interface StudentDao {
    public Collection<Faculty> getAllFaculty();
    public Availability getFacultyAvailability(String facultyID, String termName);
    public Student getStudentInfo(String studentID);
    public Appointment requestAppointment(String startTime, String endTime, String facultyID, String studentID, String date, String description, String termName);
    public Boolean cancelAppointment(Integer meetingID,String studentID);
    public Collection<Appointment> getUpcomingAppointments(String studentID);
    public Collection<Appointment> getPastAppointments(String studentID);
}