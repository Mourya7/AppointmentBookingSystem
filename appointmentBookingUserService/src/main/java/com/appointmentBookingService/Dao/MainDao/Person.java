package com.appointmentBookingService.Dao.MainDao;

import com.appointmentBookingService.Entity.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;

abstract public class Person {
    @Autowired
    JdbcTemplate jdbcTemplate;

    protected String getDepartmentName(String departmentID, String sqlQuery) {
        return jdbcTemplate.queryForObject(sqlQuery, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("name");
            }
        }, departmentID);
    }

    protected Collection<Appointment> getAllAppointments(String id,String sqlQuery) {
        return jdbcTemplate.query(sqlQuery, new RowMapper<Appointment>() {
            @Override
            public Appointment mapRow(ResultSet resultSet, int i) throws SQLException {
                Appointment appointment = new Appointment();
                appointment.setStudentID(resultSet.getString("studentID"));
                appointment.setFacultyID(resultSet.getString("facultyID"));
                appointment.setDescription(resultSet.getString("description"));
                appointment.setDate(resultSet.getString("date"));
                appointment.setStartTime(resultSet.getString("startTime"));
                appointment.setEndTime(resultSet.getString("endTime"));
                appointment.setMeetingID(resultSet.getString("meetingID"));
                appointment.setStatus(resultSet.getString("status"));
                return appointment;
            }
        },id);
    }

    protected Collection<Appointment> getUpcomingAppointments(String id, String sqlQuery) {
        LocalDate today = LocalDate.now(ZoneId.of("US/Eastern"));
        Collection<Appointment> appointments = getAllAppointments(id,sqlQuery);
        for(Appointment appointment : appointments) {
            LocalDate meetingDate = LocalDate.parse(appointment.getDate());
            if((meetingDate.isEqual(today) || meetingDate.isAfter(today)) && !(appointment.getStatus().equals("cancelled"))) {
                continue;
            }
            appointments.remove(appointment);
        }
        return appointments;
    }

    protected Collection<Appointment> getPastAppointments(String id, String sqlQuery) {
        LocalDate today = LocalDate.now(ZoneId.of("US/Eastern"));
        Collection<Appointment> appointments = getAllAppointments(id,sqlQuery);
        for(Appointment appointment : appointments) {
            LocalDate meetingDate = LocalDate.parse(appointment.getDate());
            if(meetingDate.isBefore(today) && !(appointment.getStatus().equals("cancelled"))) {
                continue;
            }
            appointments.remove(appointment);
        }
        return appointments;
    }
}
