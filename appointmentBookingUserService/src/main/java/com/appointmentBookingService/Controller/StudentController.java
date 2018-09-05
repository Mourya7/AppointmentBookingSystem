package com.appointmentBookingService.Controller;

import com.appointmentBookingService.Entity.Availability;
import com.appointmentBookingService.Entity.Faculty;
import com.appointmentBookingService.Entity.Student;
import com.appointmentBookingService.Entity.Term;
import com.appointmentBookingService.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/faculty/{id}/availability", method = RequestMethod.GET)
    public Availability getFacultyAvailability(@PathVariable("id") int id) {
        return studentService.getFacultyAvailability(id);
    }

    @RequestMapping(value="faculty/{id}")
    public Faculty getFacultyById(@PathVariable("id") int id) {
        return studentService.getFaculty(id);
    }

    @RequestMapping(value="term/{id}")
    public Term getTermById(@PathVariable("id") int id) {
        return studentService.getTerm(String.valueOf(id));
    }
}
