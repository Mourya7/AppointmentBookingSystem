package com.appointmentBookingService.Controller;

import com.appointmentBookingService.Entity.*;
import com.appointmentBookingService.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Created by mourya on 8/23/18.
 */
@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Student getStudentInfo(@PathVariable("id") int id){
        return studentService.getStudentInfo(String.valueOf(id));
    }

    @RequestMapping(value = "/faculty",method = RequestMethod.GET)
    public Collection<Faculty> getAllFaculty() {
        return studentService.getAllFaculty();
    }

    @RequestMapping(value = "/availability", method = RequestMethod.POST)
    public Collection<OfficeHours> getFacultyAvailability(@RequestBody Availability availability) {
        return studentService.getFacultyAvailability(availability);
    }

    @RequestMapping(value="/requestAppointment", method = RequestMethod.POST)
    public Appointment requestAppointment(@RequestBody Appointment appointment) {
        return studentService.requestAppointment(appointment);
    }
}