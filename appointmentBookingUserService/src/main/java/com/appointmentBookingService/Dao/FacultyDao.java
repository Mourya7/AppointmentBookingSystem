package com.appointmentBookingService.Dao;

import com.appointmentBookingService.Entity.Appointment;
import com.appointmentBookingService.Entity.Faculty;

import java.util.Collection;

/**
 * Created by mourya on 8/21/18.
 */
public interface FacultyDao {
    Faculty getFacultyInfo(int id);
    Collection<Appointment> getAllAppointment(int id);
    Collection<Appointment> getTodaysAppointment(int id);
    Collection<Appointment> getRequestedAppointments(int id);
    Collection<Appointment> getAcceptedAppointments(int id);
    Boolean acceptAppointment(int id);
}