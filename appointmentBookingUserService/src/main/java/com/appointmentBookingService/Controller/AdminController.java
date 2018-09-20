package com.appointmentBookingService.Controller;

import com.appointmentBookingService.Entity.Availability;
import com.appointmentBookingService.Entity.OfficeHours;
import com.appointmentBookingService.Entity.Term;
import com.appointmentBookingService.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @RequestMapping(value = "/addTerm", method = RequestMethod.POST)
    public Term addNewTerm(@RequestBody Term term) {
        return adminService.createTerm(term);
    }

    @RequestMapping(value="/addAvailability", method = RequestMethod.POST)
    public Availability addNewAvailability(@RequestBody Availability availability) {
        return adminService.addAvailability(availability);
    }

    @RequestMapping(value="/addOfficeHours", method = RequestMethod.POST)
    public OfficeHours addNewOfficeHours(@RequestBody OfficeHours officeHours) {
        return adminService.addOfficeHours(officeHours);
    }
}
