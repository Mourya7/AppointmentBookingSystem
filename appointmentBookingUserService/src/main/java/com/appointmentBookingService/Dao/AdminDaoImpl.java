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
@Repository("admin")
public class AdminDaoImpl implements AdminDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Term createTerm(String startDate, String endDate, String name) {
        KeyHolder key = new GeneratedKeyHolder();
        final String SQL_GET_TERM = "Select * from term where termID = ?";
        jdbcTemplate.update(new PreparedStatementCreator() {
            final String SQL_CREATE_TERM = "Insert into term (termID,startDate,endDate,name) Values (?,?,?,?)";
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement ps = connection.prepareStatement(SQL_CREATE_TERM, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1,startDate);
                ps.setString(2,endDate);
                ps.setString(3,name);
                return ps;
            }
        },key);

        return jdbcTemplate.queryForObject(SQL_GET_TERM, new RowMapper<Term>() {
            @Override
            public Term mapRow(ResultSet resultSet, int i) throws SQLException {
                Term term = new Term();
                term.setTermID(resultSet.getString("TermID"));
                term.setStartDate(resultSet.getString("startDate"));
                term.setEndDate(resultSet.getString("endDate"));
                term.setName(resultSet.getString("name"));
                return term;
            }
        },key.getKey().toString());
    }

    @Override
    public void updateTerm(String startDate, String endDate, String name) {
        final String SQL_UPDATE_TERM = "Update term set startDate = ?, endDate = ? where name = ?";
        jdbcTemplate.update(SQL_UPDATE_TERM,startDate,endDate,name);
    }

    private String getTermID(String termName) {
        final String SQL_GET_TERMID = "Select termID from term where name = ?";
        return jdbcTemplate.queryForObject(SQL_GET_TERMID,new RowMapper<String>() {

            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("termID");
            }
        },termName);
    }

    private Boolean checkExistingAvailability(String facultyId, String term) {
        final String SQL_GET_AVAILABILITY = "Select count(*) from availability where facultyID = ? and termID = ?";
        boolean result = false;
        int count = jdbcTemplate.queryForObject(SQL_GET_AVAILABILITY,new Object[]{facultyId,term},Integer.class);
        if (count > 0) {
            result = true;
        }
        return result;
    }

    @Override
    public Availability addAvailability(String facultyID, String term, int hasOfficeHours) {
        KeyHolder key = new GeneratedKeyHolder();
        final String SQL_GET_AVAILABILITY = "Select * from availability where availabilityID = ?";
        if(checkExistingAvailability(facultyID,term)) {
            return null;
        }
        jdbcTemplate.update(new PreparedStatementCreator() {
            final String SQL_CREATE_AVAILABILITY = "Insert into availability (facultyID,term,hasOfficeHours) Values (?,?,?)";
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement ps = connection.prepareStatement(SQL_CREATE_AVAILABILITY, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1,facultyID);
                ps.setString(2,getTermID(term));
                ps.setInt(3,hasOfficeHours);
                return ps;
            }
        },key);
        return jdbcTemplate.queryForObject(SQL_GET_AVAILABILITY, new RowMapper<Availability>() {
            @Override
            public Availability mapRow(ResultSet resultSet, int i) throws SQLException {
                Availability availability = new Availability();
                availability.setId(resultSet.getString("availabilityID"));
                availability.setFacultyID(resultSet.getString("facultyID"));
                availability.setTerm(resultSet.getString("term"));
                availability.setHasOfficeHours(resultSet.getInt("hasOfficeHours"));
                return availability;
            }
        },key.getKey().toString());
    }

    @Override
    public void updateAvailability(String facultyID, String term, int hasOfficeHours) {
        final String SQL_UPDATE_AVAILABILITY = "Update availability set hasOfficeHours = ? where facultyID = ? and term = ?";
        jdbcTemplate.update(SQL_UPDATE_AVAILABILITY,hasOfficeHours,facultyID,term);
    }

    private String getAvailabilityID(String facultyID,String term) {
        final String SQL_GET_AVAILABILITY = "Select availabilityID from availability where facultyID = ? and term = ?";
        if(!checkExistingAvailability(facultyID,term)) return null;
        return jdbcTemplate.queryForObject(SQL_GET_AVAILABILITY, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("availabilityID");
            }
        });
    }

    @Override
    public OfficeHours addOfficeHours(String facultyID, String term, String day, String startTime, String endTime) {
        KeyHolder key = new GeneratedKeyHolder();
        final String SQL_GET_HOURS = "Select * from officeHours where officeHoursID = ?";
        jdbcTemplate.update(new PreparedStatementCreator() {
            final String SQL_CREATE_HOURS = "Insert into officeHours (availability,day,startTime,endTime) Values (?,?,?,?)";

            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement ps = connection.prepareStatement(SQL_CREATE_HOURS, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1,getAvailabilityID(facultyID,term));
                ps.setString(2,day);
                ps.setString(3,startTime);
                ps.setString(3,endTime);
                return ps;
            }
        },key);

        return jdbcTemplate.queryForObject(SQL_GET_HOURS, new RowMapper<OfficeHours>() {
            @Override
            public OfficeHours mapRow(ResultSet resultSet, int i) throws SQLException {
                OfficeHours officeHours = new OfficeHours();
                officeHours.setId(resultSet.getString("officeHoursID"));
                officeHours.setAvailabilityID(resultSet.getString("availability"));
                officeHours.setDay(resultSet.getString("day"));
                officeHours.setStartTime(resultSet.getString("startTime"));
                officeHours.setEndTime(resultSet.getString("endTime"));
                return officeHours;
            }
        },key.getKey().toString());
    }

    @Override
    public void updateOfficeHours(String facultyID, String term, String day, String startTime, String endTime) {
        final String SQL_UPDATE_HOURS = "Update officeHours set startTime = ?, endTime = ? where facultyID = ? and term = ? and day = ?";
        jdbcTemplate.update(SQL_UPDATE_HOURS,startTime,endTime,facultyID,term,day);
    }
}
