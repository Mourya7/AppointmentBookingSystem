package com.appointmentBookingService.Dao;

import com.appointmentBookingService.Dao.MainDao.Person;
import com.appointmentBookingService.Entity.Appointment;
import com.appointmentBookingService.Entity.Faculty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;

/**
 * Created by mourya on 8/21/18.
 */
@Repository("faculty")
public class FacultyDaoImpl extends Person implements FacultyDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Faculty getFacultyInfo(String facultyID) {
        final String getStudentInfo = "Select * from faculty where facultyID = ?";
        return jdbcTemplate.queryForObject(getStudentInfo, new RowMapper<Faculty>() {
            final String SQL_GET_DEPT_NAME = "SELECT name FROM department WHERE departmentID = ?";
            @Override
            public Faculty mapRow(ResultSet resultSet, int i) throws SQLException {
                Faculty faculty = new Faculty();
                faculty.setFacultyId(resultSet.getString("facultyID"));
                faculty.setName(resultSet.getString("name"));
                faculty.setEmail(resultSet.getString("email"));
                faculty.setDepartmentName(getDepartmentName(resultSet.getString("department"),SQL_GET_DEPT_NAME));
                faculty.setPhone(resultSet.getString("phone"));
                return faculty;
            }
        }, facultyID);
    }

    @Override
    public Collection<Appointment> getTodaysAppointment(String facultyID) {
        final String SQL_GET_ALL_APPOINTMENT = "Select * from appointment where facultyID = ?";
        LocalDate today = LocalDate.now(ZoneId.of("US/Eastern"));
        Collection<Appointment> appointments = getAllAppointments(facultyID,SQL_GET_ALL_APPOINTMENT);
        for(Appointment appointment : appointments) {
            LocalDate meetingDate = LocalDate.parse(appointment.getDate());
            if(meetingDate.isEqual(today) && !(appointment.getStatus().equals("cancelled"))) {
                continue;
            }
            appointments.remove(appointment);
        }
        return appointments;
    }

    @Override
    public Collection<Appointment> getRequestedAppointments(String facultyID) {
        final String SQL_GET_REQUESTED_APPTS = "Select * from appointment where facultyID = ? and status = 'pending'";
        Collection<Appointment> appointments = getAllAppointments(facultyID,SQL_GET_REQUESTED_APPTS);
        LocalDate today = LocalDate.now(ZoneId.of("US/Eastern"));
        for(Appointment appointment : appointments) {
            LocalDate meetingDate = LocalDate.parse(appointment.getDate());
            if(meetingDate.isEqual(today) || meetingDate.isAfter(today)) {
                continue;
            }
            appointments.remove(appointment);
        }
        return appointments;
    }

    @Override
    public Collection<Appointment> getAcceptedAppointments(String facultyID) {
        final String SQL_GET_ACCEPTED_APPTS = "Select * from appointment where facultyID = ? and status = 'accepted'";
        Collection<Appointment> appointments = getAllAppointments(facultyID,SQL_GET_ACCEPTED_APPTS);
        LocalDate today = LocalDate.now(ZoneId.of("US/Eastern"));
        for(Appointment appointment : appointments) {
            LocalDate meetingDate = LocalDate.parse(appointment.getDate());
            if(meetingDate.isEqual(today) || meetingDate.isAfter(today)) {
                continue;
            }
            appointments.remove(appointment);
        }
        return appointments;
    }

    @Override
    public Boolean acceptAppointment(String meetingID,String facultyID) {
        final String SQL_ACCEPT_APPOINTMENT = "Update appointment Set status = 'accepted' where meetingID = ? and facultyID = ?";
        return jdbcTemplate.update(SQL_ACCEPT_APPOINTMENT,new Object[]{meetingID,facultyID}) > 0;
    }

    @Override
    public Boolean cancelAppointment(String meetingID, String facultyID) {
        final String SQL_CANCEL_APPOINTMENT = "Update appointment Set status = 'cancelled' where meetingID = ? and facultyID = ?";
        return jdbcTemplate.update(SQL_CANCEL_APPOINTMENT,new Object[]{meetingID,facultyID}) > 0;
    }

    @Override
    public Collection<Appointment> getUpcomingAppointments(String facultyID) {
        final String SQL_GET_ALL_APPOINTMENT = "Select * from appointment where facultyID = ?";
        return super.getUpcomingAppointments(facultyID,SQL_GET_ALL_APPOINTMENT);
    }

    @Override
    public Collection<Appointment> getPastAppointments(String facultyID) {
        final String SQL_GET_ALL_APPOINTMENT = "Select * from appointment where facultyID = ?";
        return super.getPastAppointments(facultyID,SQL_GET_ALL_APPOINTMENT);
    }
}
