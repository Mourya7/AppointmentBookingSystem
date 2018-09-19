package com.appointmentBookingService.Dao;

import com.appointmentBookingService.Entity.*;

import java.util.Collection;

/**
 * Created by mourya on 8/21/18.
 */
public interface StudentDao {
    public Collection<Faculty> getAllFaculty();
    public Faculty getFacultyById(int id);
    public Availability getFacultyAvailability(int id);
    public Student getStudentInfo(String id);
    public Appointment requestAppointment(String startTime, String endTime, String facultyID, String studentID, String date, String description, String termName);
    public Boolean cancelAppointment(String meetingID,String studentID);
    public Collection<Appointment> getUpcomingAppointments(String id);
    public Collection<Appointment> getPastAppointments(String id);
}