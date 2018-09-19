package com.appointmentBookingService.Controller;

import com.appointmentBookingService.Entity.Appointment;
import com.appointmentBookingService.Entity.Faculty;
import com.appointmentBookingService.Service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    @Autowired
    private FacultyService facultyService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Faculty getFacultyInfo(@PathVariable("id") int id) {
        return facultyService.getFacultyInfo(String.valueOf(id));
    }

    @RequestMapping(value="/{id}/{today}",method = RequestMethod.GET)
    public Collection<Appointment> getTodaysAppointment(@PathVariable("id") int id) {
        return facultyService.getTodaysAppointment(String.valueOf(id));
    }

    @RequestMapping(value="/{id}/requests",method = RequestMethod.GET)
    public Collection<Appointment> getRequestedAppointments(@PathVariable("id") int id) {
        return facultyService.getRequestedAppointments(String.valueOf(id));
    }

    @RequestMapping(value="/{id}/accepted",method = RequestMethod.GET)
    public Collection<Appointment> getAcceptedAppointments(@PathVariable("id") int id) {
        return facultyService.getAcceptedAppointments(String.valueOf(id));
    }

    @RequestMapping(value="/{id}/accept/{meetingID}",method = RequestMethod.PUT)
    public Boolean acceptAppointment(@PathVariable("id") int facultyID, @PathVariable("meetingID") int meetingID) {
        return facultyService.acceptAppointment(String.valueOf(meetingID),String.valueOf(facultyID));
    }

    @RequestMapping(value = "/{id}/cancel/{meetingID}",method = RequestMethod.PUT)
    public Boolean cancelAppointment(@PathVariable("id") int facultyID, @PathVariable("meetingID") int meetingID) {
        return facultyService.cancelAppointment(String.valueOf(meetingID),String.valueOf(facultyID));
    }
}
