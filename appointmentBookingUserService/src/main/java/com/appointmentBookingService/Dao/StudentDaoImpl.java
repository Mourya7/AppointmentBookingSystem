package com.appointmentBookingService.Dao;

import com.appointmentBookingService.Dao.MainDao.Person;
import com.appointmentBookingService.Entity.*;
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
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;

/**
 * Created by mourya on 8/21/18.
 */
@Repository("student")
public class StudentDaoImpl extends Person implements StudentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Student getStudentInfo(String studentID) {
        final String SQL_GET_STUDENT = "SELECT * FROM student WHERE universityID = ?";
        return jdbcTemplate.queryForObject(SQL_GET_STUDENT, new RowMapper<Student>() {
            @Override
            public Student mapRow(ResultSet resultSet, int i) throws SQLException {
                Student student = new Student();
                student.setDepartmentName(getDepartmentName(resultSet.getString("departmentID")));
                student.setEmail(resultSet.getString("email"));
                student.setName(resultSet.getString("name"));
                student.setUniversityID(resultSet.getString("universityID"));
                return student;
            }
        }, studentID);
    }

    private class FacultyRowMapper implements RowMapper<Faculty> {
        @Override
        public Faculty mapRow(ResultSet resultSet, int i) throws SQLException {
            Faculty faculty = new Faculty();
            faculty.setFacultyId(resultSet.getString("facultyID"));
            faculty.setName(resultSet.getString("name"));
            faculty.setEmail(resultSet.getString("email"));
            faculty.setDepartmentName(getDepartmentName(resultSet.getString("departmentID")));
            return faculty;
        }
    }

    @Override
    public Collection<Faculty> getAllFaculty() {
        final String SQL_GET_ALL_FACULTY = "SELECT * FROM faculty";
        List<Faculty> faculties = jdbcTemplate.query(SQL_GET_ALL_FACULTY, new FacultyRowMapper());
        return faculties;
    }

    @Override
    public Collection<OfficeHours> getFacultyAvailability(String facultyID, String termName) {
        Integer termID = getTermIDByName(termName);
        final String SQL_GET_FACULTY_AVAILABILITY = "SELECT availabilityID FROM availability where facultyID = ? and termID = ?";
        Integer availabilityID = jdbcTemplate.queryForObject(SQL_GET_FACULTY_AVAILABILITY, new RowMapper<Integer>() {

            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getInt("availabilityID");
            }
        }, facultyID,termID);
        return getOfficeHoursByAvailabilityID(availabilityID);
    }

    private Collection<OfficeHours> getOfficeHoursByAvailabilityID(Integer availabilityID) {
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

    private Integer getTermIDByName(String termName) {
        final String SQL_GET_TERM_ID = "Select termID from term where name = ?";
        return jdbcTemplate.queryForObject(SQL_GET_TERM_ID, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getInt("termID");
            }
        },termName);
    }

    @Override
    public Appointment requestAppointment(String meetingStartTime, String meetingEndTime,String facultyID, String studentID, String date, String description, String termName) {
        String day = LocalDate.parse(date).getDayOfWeek().toString().toLowerCase();
        if(!isValidRequest(meetingStartTime,meetingEndTime,date,facultyID,getTermIDByName(termName),day)) return null;
        return addNewMeetingRequest(meetingStartTime, meetingEndTime, facultyID, studentID, date,description);
    }

    private boolean isValidRequest(String meetingStartTime, String meetingEndTime, String date, String facultyID, Integer termID, String day) {

        //convert string to localDate
        LocalDate meetingDate = LocalDate.parse(date);
        LocalTime startTime = LocalTime.parse(meetingStartTime);
        LocalTime endTime = LocalTime.parse(meetingEndTime);
        LocalTime currentTime = LocalTime.now(ZoneId.of("US/Eastern"));
        LocalDate currentDate = LocalDate.now(ZoneId.of("US/Eastern"));

        if(checkExistingMeeting(meetingDate,facultyID,meetingStartTime) > 0) return false;

        //check if meeting date is before current date
        if(meetingDate.isBefore(currentDate)) {
            return false;
        }

        if(meetingDate.isEqual(currentDate) && startTime.isBefore(currentTime)) {
            return false;
        }
        //Check if the faculty has office hours or not
        Integer hasOfficeHours = getHasFacultyOfficeHours(facultyID,termID);

        if(hasOfficeHours != 1) {
            return false;
        }

        Term term = getTermInfo(termID);

        //get current term starting date and ending date
        LocalDate termStartDate = LocalDate.parse(term.getStartDate());
        LocalDate termEndDate = LocalDate.parse(term.getEndDate());


        if(termStartDate.isBefore(meetingDate) && termEndDate.isAfter(meetingDate)) {
            Collection<OfficeHours> officeHours = getOfficeHoursByDay(facultyID,termID,day);
            //if faculty has multiple officeHours on same day - check if meeting time coincides with at least one of the office hours
            boolean validTime = false;
            for(OfficeHours officeHour : officeHours) {
                LocalTime officeHourStart = LocalTime.parse(officeHour.getStartTime());
                LocalTime officeHourEnd = LocalTime.parse(officeHour.getEndTime());
                if((startTime.isAfter(officeHourStart) || startTime.equals(officeHourStart))
                        && (endTime.isBefore(officeHourEnd) || endTime.equals(officeHourEnd))) {
                    validTime = true;
                }
            }
            if(!validTime) {
                return false;
            }
        }
        return false;
    }

    private Term getTermInfo(Integer termID) {
        final String getTermInfo = "Select * from term where termID = ?";
        return jdbcTemplate.queryForObject(getTermInfo, new RowMapper<Term>() {
            @Override
            public Term mapRow(ResultSet resultSet, int i) throws SQLException {
                Term term = new Term();
                term.setStartDate(resultSet.getString("startDate"));
                term.setEndDate(resultSet.getString("endDate"));
                term.setName(resultSet.getString("name"));
                term.setTermID(resultSet.getInt("termID"));
                return term;
            }
        },termID);
    }

    private Integer getHasFacultyOfficeHours(String facultyID, Integer termID) {
        //Every Faculty has exactly one availability for a given term {fall/summer/spring}
        final String SQL_GET_FACULTY_AVAILABILITY = "SELECT hasOfficeHours FROM availability where facultyID = ? and term = ?";
        return jdbcTemplate.queryForObject(SQL_GET_FACULTY_AVAILABILITY, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getInt("hasOfficeHours");
            }
        }, facultyID, termID);
    }

    private Collection<OfficeHours> getOfficeHoursByDay(String facultyID,Integer termID, String day) {
        //Nested query to get faculty availabilityID for that term and fetch the office hour start time and end time
        final String SQL_GET_OFFICE_HOURS = "Select startTime,endTime from officeHours where day = ? and availability in " +
                "(Select availabilityID from availability where facultyID = ? and hasOfficeHours = 1 and termID = ?)";
        return jdbcTemplate.query(SQL_GET_OFFICE_HOURS, new RowMapper<OfficeHours>() {
            @Override
            public OfficeHours mapRow(ResultSet resultSet, int i) throws SQLException {
                OfficeHours officeHours = new OfficeHours();
                officeHours.setStartTime(resultSet.getString("startTime"));
                officeHours.setEndTime(resultSet.getString("endTime"));
                return officeHours;
            }
        },day,facultyID,termID);
    }

    private Integer checkExistingMeeting(LocalDate date,String facultyID, String startTime) {
        final String SQL_CHECK_EXISTING_MEETING = "Select count(*) from appointment where startTime = ? and facultyID = ? and date = ?";
        return jdbcTemplate.queryForObject(SQL_CHECK_EXISTING_MEETING,new Object[]{startTime,facultyID,date},Integer.class);
    }

    private Appointment addNewMeetingRequest(String startTime, String endTime, String facultyID, String studentID, String date, String description) {
        KeyHolder key = new GeneratedKeyHolder();
        final String status = "pending";
        jdbcTemplate.update(new PreparedStatementCreator() {
            final String SQL_INSERT_APPOINTMENT = "Insert into appointment (studentID,facultyID,startTime,endTime,date,description,status) Values " +
                    "(?,?,?,?,?,?,?)";
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement ps = connection.prepareStatement(SQL_INSERT_APPOINTMENT, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1,studentID);
                ps.setString(2,facultyID);
                ps.setString(3,startTime);
                ps.setString(4,endTime);
                ps.setString(5,date);
                ps.setString(6,description);
                ps.setString(7,status);
                return ps;
            }
        },key);
        final String SQL_GET_APPOINTMENT = "Select * from appointment where meetingID = ?";
        return jdbcTemplate.queryForObject(SQL_GET_APPOINTMENT, new RowMapper<Appointment>() {
            @Override
            public Appointment mapRow(ResultSet resultSet,int i) throws SQLException {
                Appointment appointment = new Appointment();
                appointment.setMeetingID(resultSet.getInt("meetingID"));
                appointment.setStudentID(resultSet.getString("studentID"));
                appointment.setFacultyID(resultSet.getString("facultyID"));
                appointment.setStartTime(resultSet.getString("startTime"));
                appointment.setEndTime(resultSet.getString("endTime"));
                appointment.setDate(resultSet.getString("date"));
                appointment.setDescription(resultSet.getString("description"));
                appointment.setStatus("status");
                return appointment;
            }
        },key.getKey().intValue());
    }

    @Override
    public Boolean cancelAppointment(Integer meetingID, String studentID) {
        final String SQL_CANCEL_APPOINTMENT = "Update appointment Set status = 'cancelled' where meetingID = ? and studentID = ?";
        return jdbcTemplate.update(SQL_CANCEL_APPOINTMENT,new Object[]{meetingID,studentID}) > 0;
    }

    @Override
    public Collection<Appointment> getUpcomingAppointments(String studentID) {
        final String SQL_GET_ALL_APPOINTMENT = "Select * from appointment where studentID = ?";
        return super.getUpcomingAppointments(studentID,SQL_GET_ALL_APPOINTMENT);
    }

    @Override
    public Collection<Appointment> getPastAppointments(String studentID) {
        final String SQL_GET_ALL_APPOINTMENT = "Select * from appointment where studentID = ?";
        return super.getPastAppointments(studentID,SQL_GET_ALL_APPOINTMENT);
    }
}