package com.appointmentBookingService.Dao;

import com.appointmentBookingService.Entity.*;

import java.util.Collection;
import java.util.Date;

/**
 * Created by mourya on 8/21/18.
 */
public interface StudentDao {
    public Collection<Faculty> getAllFaculty();
    public Faculty getFacultyById(int id);
    public Availability getFacultyAvailability(int id);
    public Appointment getStudentAppointments(int id);
    public Student getStudentInfo(String id);
    public Appointment requestAppointment(String startTime, String endTime, String facultyID, String studentID, String date, String description);
    public Term getTermInfo(String id);
}