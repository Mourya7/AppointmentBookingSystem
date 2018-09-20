package com.appointmentBookingService.Service;

import com.appointmentBookingService.Dao.AdminDAO;
import com.appointmentBookingService.Entity.Availability;
import com.appointmentBookingService.Entity.OfficeHours;
import com.appointmentBookingService.Entity.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    @Qualifier("admin")
    private AdminDAO adminDao;

    public Term createTerm(Term term) {
        return adminDao.createTerm(term.getStartDate(),term.getEndDate(),term.getName());
    }

    public Availability addAvailability(Availability availability) {
        return adminDao.addAvailability(availability.getFacultyID(),availability.getTermName(),availability.getHasOfficeHours());
    }

    public OfficeHours addOfficeHours(OfficeHours officeHours) {
        return adminDao.addOfficeHours(officeHours.getFacultyID(),officeHours.getTermName(),
                officeHours.getDay(),officeHours.getStartTime(),officeHours.getEndTime());
    }
}
