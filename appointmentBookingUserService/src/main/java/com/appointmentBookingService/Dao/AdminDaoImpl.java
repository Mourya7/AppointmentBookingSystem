package com.appointmentBookingService.Dao;

import com.appointmentBookingService.Entity.Availability;
import com.appointmentBookingService.Entity.OfficeHours;
import com.appointmentBookingService.Entity.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

@Repository("admin")
public class AdminDaoImpl implements AdminDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Term createTerm(String startDate, String endDate, String termName) {
        KeyHolder key = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            final String SQL_CREATE_TERM = "Insert into term (startDate,endDate,name) Values (?,?,?)";
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement ps = connection.prepareStatement(SQL_CREATE_TERM, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1,startDate);
                ps.setString(2,endDate);
                ps.setString(3,termName);
                return ps;
            }
        },key);

        final String SQL_GET_TERM = "Select * from term where termID = ?";
        return jdbcTemplate.queryForObject(SQL_GET_TERM, new RowMapper<Term>() {
            @Override
            public Term mapRow(ResultSet resultSet, int i) throws SQLException {
                Term term = new Term();
                term.setTermID(resultSet.getInt("TermID"));
                term.setStartDate(resultSet.getString("startDate"));
                term.setEndDate(resultSet.getString("endDate"));
                term.setName(resultSet.getString("name"));
                return term;
            }
        },key.getKey().intValue());
    }

    @Override
    public void updateTerm(String startDate, String endDate, String termName) {
        final String SQL_UPDATE_TERM = "Update term set startDate = ?, endDate = ? where name = ?";
        jdbcTemplate.update(SQL_UPDATE_TERM,startDate,endDate,termName);
    }

    private Integer getTermID(String termName) {
        final String SQL_GET_TERMID = "Select termID from term where name = ?";
        return jdbcTemplate.queryForObject(SQL_GET_TERMID,new RowMapper<Integer>() {

            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getInt("termID");
            }
        },termName);
    }

    private Boolean checkExistingAvailability(String facultyId, String termName) {
        final String SQL_GET_AVAILABILITY = "Select count(*) from availability where facultyID = ? and termID = ?";
        boolean result = false;
        int count = jdbcTemplate.queryForObject(SQL_GET_AVAILABILITY,new Object[]{facultyId,getTermID(termName)},Integer.class);
        if (count > 0) {
            result = true;
        }
        return result;
    }

    @Override
    public Availability addAvailability(String facultyID, String termName, int hasOfficeHours) {
        KeyHolder key = new GeneratedKeyHolder();
        if(checkExistingAvailability(facultyID,termName)) {
            return null;
        }
        jdbcTemplate.update(new PreparedStatementCreator() {
            final String SQL_CREATE_AVAILABILITY = "Insert into availability (facultyID,termID,hasOfficeHours) Values (?,?,?)";
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement ps = connection.prepareStatement(SQL_CREATE_AVAILABILITY, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1,facultyID);
                ps.setInt(2,getTermID(termName));
                ps.setInt(3,hasOfficeHours);
                return ps;
            }
        },key);

        final String SQL_GET_AVAILABILITY = "Select * from availability where availabilityID = ?";
        return jdbcTemplate.queryForObject(SQL_GET_AVAILABILITY, new RowMapper<Availability>() {
            @Override
            public Availability mapRow(ResultSet resultSet, int i) throws SQLException {
                Availability availability = new Availability();
                availability.setAvailabilityID(resultSet.getInt("availabilityID"));
                availability.setFacultyID(resultSet.getString("facultyID"));
                availability.setTermID(resultSet.getInt("termID"));
                availability.setHasOfficeHours(resultSet.getInt("hasOfficeHours"));
                return availability;
            }
        },key.getKey().intValue());
    }

    @Override
    public void updateAvailability(String facultyID, String term, int hasOfficeHours) {
        final String SQL_UPDATE_AVAILABILITY = "Update availability set hasOfficeHours = ? where facultyID = ? and term = ?";
        jdbcTemplate.update(SQL_UPDATE_AVAILABILITY,hasOfficeHours,facultyID,term);
    }

    private Integer getAvailabilityID(String facultyID,String termName) {
        final String SQL_GET_AVAILABILITY = "Select availabilityID from availability where facultyID = ? and termID = ?";
        if(!checkExistingAvailability(facultyID,termName)) return null;
        return jdbcTemplate.queryForObject(SQL_GET_AVAILABILITY, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getInt("availabilityID");
            }
        },facultyID,getTermID(termName));
    }

    @Override
    public OfficeHours addOfficeHours(String facultyID, String termName, String day, String startTime, String endTime) {
        if(!isValidOfficeHours(facultyID, termName, day, startTime, endTime)) return null;
        KeyHolder key = new GeneratedKeyHolder();
        final String SQL_GET_HOURS = "Select * from officeHours where officeHoursID = ?";
        jdbcTemplate.update(new PreparedStatementCreator() {
            final String SQL_CREATE_HOURS = "Insert into officeHours (availability,day,startTime,endTime) Values (?,?,?,?)";

            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement ps = connection.prepareStatement(SQL_CREATE_HOURS, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1,getAvailabilityID(facultyID,termName));
                ps.setString(2,day);
                ps.setString(3,startTime);
                ps.setString(4,endTime);
                return ps;
            }
        },key);

        return jdbcTemplate.queryForObject(SQL_GET_HOURS, new RowMapper<OfficeHours>() {
            @Override
            public OfficeHours mapRow(ResultSet resultSet, int i) throws SQLException {
                OfficeHours officeHours = new OfficeHours();
                officeHours.setOfficeHoursID(resultSet.getInt("officeHoursID"));
                officeHours.setAvailabilityID(resultSet.getInt("availability"));
                officeHours.setDay(resultSet.getString("day"));
                officeHours.setStartTime(resultSet.getString("startTime"));
                officeHours.setEndTime(resultSet.getString("endTime"));
                officeHours.setFacultyID(facultyID);
                return officeHours;
            }
        },key.getKey().intValue());
    }

    private Collection<OfficeHours> existingOfficeHours(Integer availabilityID,String day) {
        final String SQL_GET_OFFICE_HOURS = "Select * from officeHours where availability = ? and day = ?";
        return jdbcTemplate.query(SQL_GET_OFFICE_HOURS, new RowMapper<OfficeHours>() {

            @Override
            public OfficeHours mapRow(ResultSet resultSet, int i) throws SQLException {
                OfficeHours officeHours = new OfficeHours();
                officeHours.setAvailabilityID(resultSet.getInt("availability"));
                officeHours.setDay(resultSet.getString("day"));
                officeHours.setStartTime(resultSet.getString("startTime"));
                officeHours.setEndTime(resultSet.getString("endTime"));
                return officeHours;
            }
        },availabilityID,day);
    }

    private boolean isValidOfficeHours(String facultyID, String termName, String day, String startTime, String endTime) {
        LocalTime minTime = LocalTime.parse("08:00:00");
        LocalTime maxTime = LocalTime.parse("16:00:00");
        LocalTime officeStart = LocalTime.parse(startTime);
        LocalTime officeEnd = LocalTime.parse(endTime);
        if(officeStart.isBefore(minTime) || officeStart.isAfter(maxTime)) return false;
        if(officeEnd.isBefore(officeStart) || officeEnd.isAfter(maxTime)) return false;
        Integer availabilityID = getAvailabilityID(facultyID,termName);
        Collection<OfficeHours> officeHours = existingOfficeHours(availabilityID,day);
        for(OfficeHours officeHour : officeHours) {
            if(day.equalsIgnoreCase(officeHour.getDay())) {
                LocalTime tempStart = LocalTime.parse(officeHour.getStartTime());
                LocalTime tempEnd = LocalTime.parse(officeHour.getEndTime());
                //weak check.. Need to implement a better algorithm
                if(officeStart.equals(tempStart) || officeStart.equals(tempEnd)) return false;
                if(officeEnd.equals(tempStart) || officeEnd.equals(tempEnd)) return false;
            }
        }
        return true;
    }

    @Override
    public void updateOfficeHours(String facultyID, String term, String day, String startTime, String endTime) {

    }
}
