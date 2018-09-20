package com.appointmentBookingService.Dao.MainDao;

import com.appointmentBookingService.Entity.Appointment;
import com.appointmentBookingService.Entity.OfficeHours;
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

    protected String getDepartmentName(String departmentID) {
        final String SQL_GET_DEPT_NAME = "SELECT name FROM department WHERE departmentID = ?";
        return jdbcTemplate.queryForObject(SQL_GET_DEPT_NAME, new RowMapper<String>() {
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
                appointment.setMeetingID(resultSet.getInt("meetingID"));
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

    protected Collection<OfficeHours> getFacultyAvailability(String facultyID, Integer termID) {
        final String SQL_GET_FACULTY_AVAILABILITY = "SELECT availabilityID FROM availability where facultyID = ? and termID = ?";
        Integer availabilityID = jdbcTemplate.queryForObject(SQL_GET_FACULTY_AVAILABILITY, new RowMapper<Integer>() {

            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getInt("availabilityID");
            }
        }, facultyID,termID);
        return getOfficeHoursByAvailabilityID(availabilityID);
    }

    protected Collection<OfficeHours> getOfficeHoursByAvailabilityID(Integer availabilityID) {
        final String SQL_GET_OFFICE_HOURS = "Select * from officeHours where availability = ?";
        return jdbcTemplate.query(SQL_GET_OFFICE_HOURS, new RowMapper<OfficeHours>() {
            @Override
            public OfficeHours mapRow(ResultSet resultSet, int i) throws SQLException {
                OfficeHours officeHours = new OfficeHours();
                officeHours.setDay(resultSet.getString("day"));
                officeHours.setStartTime(resultSet.getString("startTime"));
                officeHours.setEndTime(resultSet.getString("endTime"));
                return officeHours;
            }
        },availabilityID);
    }
}
