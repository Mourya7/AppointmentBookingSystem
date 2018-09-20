package com.appointmentBookingService.Controller;

import com.appointmentBookingService.Entity.Appointment;
import com.appointmentBookingService.Entity.Faculty;
import com.appointmentBookingService.Service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value="/today",method = RequestMethod.POST)
    public Collection<Appointment> getTodaysAppointments(@RequestBody Faculty faculty) {
        return facultyService.getTodaysAppointment(faculty.getFacultyId());
    }

    @RequestMapping(value="/requests",method = RequestMethod.POST)
    public Collection<Appointment> getRequestedAppointments(@RequestBody Faculty faculty) {
        return facultyService.getRequestedAppointments(faculty.getFacultyId());
    }

    @RequestMapping(value="/accepted",method = RequestMethod.POST)
    public Collection<Appointment> getAcceptedAppointments(@RequestBody Faculty faculty) {
        return facultyService.getAcceptedAppointments(faculty.getFacultyId());
    }

    @RequestMapping(value="/accept",method = RequestMethod.PUT)
    public Boolean acceptAppointment(@RequestBody Appointment appointment) {
        return facultyService.acceptAppointment(appointment.getMeetingID(),appointment.getFacultyID());
    }

    @RequestMapping(value = "/cancel",method = RequestMethod.PUT)
    public Boolean cancelAppointment(@RequestBody Appointment appointment) {
        return facultyService.cancelAppointment(appointment.getMeetingID(),appointment.getFacultyID());
    }
}
