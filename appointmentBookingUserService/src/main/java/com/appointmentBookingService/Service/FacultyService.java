package com.appointmentBookingService.Service;

import com.appointmentBookingService.Dao.FacultyDao;
import com.appointmentBookingService.Entity.Appointment;
import com.appointmentBookingService.Entity.Faculty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class FacultyService {
    @Autowired
    @Qualifier("faculty")
    private FacultyDao facultyDao;

    public Faculty getFacultyInfo(String facultyID) {
        return facultyDao.getFacultyInfo(facultyID);
    }

    public Collection<Appointment> getTodaysAppointment(String facultyID) {
        return facultyDao.getTodaysAppointment(facultyID);
    }

    public Collection<Appointment> getRequestedAppointments(String facultyID) {
        return facultyDao.getRequestedAppointments(facultyID);
    }

    public Collection<Appointment> getAcceptedAppointments(String facultyID) {
        return facultyDao.getAcceptedAppointments(facultyID);
    }

    public Boolean acceptAppointment(String meetingID,String facultyID) {
        return facultyDao.acceptAppointment(meetingID,facultyID);
    }

    public Boolean cancelAppointment(String meetingID,String facultyID) {
        return facultyDao.cancelAppointment(meetingID,facultyID);
    }

    public Collection<Appointment> getUpcomingAppointments(String facultyID) {
        return facultyDao.getUpcomingAppointments(facultyID);
    }

    public Collection<Appointment> getPastAppointments(String facultyID) {
        return facultyDao.getPastAppointments(facultyID);
    }
}
