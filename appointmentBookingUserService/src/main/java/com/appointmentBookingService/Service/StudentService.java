package com.appointmentBookingService.Service;

import com.appointmentBookingService.Dao.StudentDao;
import com.appointmentBookingService.Entity.Availability;
import com.appointmentBookingService.Entity.Faculty;
import com.appointmentBookingService.Entity.Student;
import com.appointmentBookingService.Entity.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by mourya on 8/23/18.
 */
@Service
public class StudentService {
    @Autowired
    @Qualifier("mysql")
    private StudentDao studentDao;
    public Student getStudentInfo(String id){
        return this.studentDao.getStudentInfo(id);
    }

    public Collection<Faculty> getAllFaculty() {
        return this.studentDao.getAllFaculty();
    }

    public Availability getFacultyAvailability(int id) {
        return this.studentDao.getFacultyAvailability(id);
    }

    public Faculty getFaculty(int id) {
        return this.studentDao.getFacultyById(id);
    }

    public Term getTerm(String id) { return this.studentDao.getTermInfo(id);}
}
