package com.appointmentBookingService.Dao;

import com.appointmentBookingService.Entity.Appointment;
import com.appointmentBookingService.Entity.Faculty;

import java.util.Collection;

/**
 * Created by mourya on 8/21/18.
 */
public interface FacultyDao {
    Faculty getFacultyInfo(String id);
    Collection<Appointment> getTodaysAppointment(String id);
    Collection<Appointment> getRequestedAppointments(String id);
    Collection<Appointment> getAcceptedAppointments(String id);
    void acceptAppointment(String id);
    void cancelAppointment(String meetingID);
    Collection<Appointment> getUpcomingAppointments(String id);
    Collection<Appointment> getPastAppointments(String id);
}