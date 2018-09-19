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

    private class FacultyRowMapper implements RowMapper<Faculty> {
        final String SQL_GET_DEPT_NAME = "SELECT name FROM department WHERE departmentID = ?";
        @Override
        public Faculty mapRow(ResultSet resultSet, int i) throws SQLException {
            Faculty faculty = new Faculty();
            faculty.setFacultyId(resultSet.getString("facultyID"));
            faculty.setName(resultSet.getString("name"));
            faculty.setEmail(resultSet.getString("email"));
            faculty.setDepartmentName(getDepartmentName(resultSet.getString("department"),SQL_GET_DEPT_NAME));
            return faculty;
        }
    }

    @Override
    public Collection<Faculty> getAllFaculty() {
        final String getAllFaculty = "SELECT facultyID,name,department,email,phone FROM faculty";
        List<Faculty> faculties = jdbcTemplate.query(getAllFaculty, new FacultyRowMapper());
        return faculties;
    }

    @Override
    public Faculty getFacultyById(int id) {
        final String getFacultyById = "SELECT facultyID,name,department,email,phone FROM faculty where facultyID = ?";
        return jdbcTemplate.queryForObject(getFacultyById, new FacultyRowMapper(), id);
    }

    @Override
    public Availability getFacultyAvailability(int id) {
        final String getFacultyAvailability = "SELECT id,facultyID,term,hasOfficeHours FROM availability where facultyID = ?";
        return jdbcTemplate.queryForObject(getFacultyAvailability, new RowMapper<Availability>() {

            @Override
            public Availability mapRow(ResultSet resultSet, int i) throws SQLException {
                Availability availability = new Availability();
                availability.setFacultyID(resultSet.getString("facultyID"));
                availability.setId(resultSet.getString("id"));
                availability.setTerm(resultSet.getString("term"));
                availability.setHasOfficeHours(resultSet.getInt("hasOfficeHours"));
                return availability;
            }
        }, id);
    }

    private Integer isFacultyOfficeHours(String facultyID, String termID) {
        //Every Faculty has exactly one availability for a given term {fall/summer/spring}
        final String getFacultyAvailability = "SELECT hasOfficeHours FROM availability where facultyID = ? and term = ?";
        return jdbcTemplate.queryForObject(getFacultyAvailability, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getInt("hasOfficeHours");
            }
        }, facultyID, termID);
    }

    @Override
    public Student getStudentInfo(String id) {
        final String getStudentInfo = "Select * from student where universityID = ?";
        final String SQL_GET_DEPT_NAME = "SELECT name FROM department WHERE departmentID = ?";
        return jdbcTemplate.queryForObject(getStudentInfo, new RowMapper<Student>() {
            @Override
            public Student mapRow(ResultSet resultSet, int i) throws SQLException {
                Student student = new Student();
                student.setDepartmentName(getDepartmentName(resultSet.getString("department"),SQL_GET_DEPT_NAME));
                student.setEmail(resultSet.getString("email"));
                student.setName(resultSet.getString("name"));
                student.setUniversityID(resultSet.getString("universityID"));
                return student;
            }
        }, id);
    }

    private String getTermIDByName(String name) {
        final String SQL_GET_TERM_ID = "Select * from term where name = ?";
        return jdbcTemplate.queryForObject(SQL_GET_TERM_ID, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("termID");
            }
        },name);
    }

    @Override
    public Boolean cancelAppointment(String meetingID, String studentID) {
        final String SQL_CANCEL_APPOINTMENT = "Update appointment Set status = 'cancelled' where meetingID = ? and studentID = ?";
        return jdbcTemplate.update(SQL_CANCEL_APPOINTMENT,new Object[]{meetingID,studentID}) > 0;
    }

    @Override
    public Collection<Appointment> getUpcomingAppointments(String id) {
        final String SQL_GET_ALL_APPOINTMENT = "Select * from appointment where studentID = ?";
        return super.getUpcomingAppointments(id,SQL_GET_ALL_APPOINTMENT);
    }

    @Override
    public Collection<Appointment> getPastAppointments(String id) {
        final String SQL_GET_ALL_APPOINTMENT = "Select * from appointment where studentID = ?";
        return super.getPastAppointments(id,SQL_GET_ALL_APPOINTMENT);
    }

    @Override
    public Appointment requestAppointment(String startTime, String endTime,String facultyID, String studentID, String date, String description, String termName) {
        String day = LocalDate.parse(date).getDayOfWeek().toString().toLowerCase();
        if(!isValidRequest(startTime,endTime,date,facultyID,getTermIDByName(termName),day)) return null;
        return addNewMeetingRequest(startTime, endTime, facultyID, studentID, date,description);
    }

    private Appointment addNewMeetingRequest(String startTime, String endTime, String facultyID, String studentID, String date, String description) {
        KeyHolder key = new GeneratedKeyHolder();
        final String status = "pending";
        final String SQL_GET_APPOINTMENT = "Select * from appointment where meetingID = ?";
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
        return jdbcTemplate.queryForObject(SQL_GET_APPOINTMENT, new RowMapper<Appointment>() {
            @Override
            public Appointment mapRow(ResultSet resultSet,int i) throws SQLException {
                Appointment appointment = new Appointment();
                appointment.setMeetingID(resultSet.getString("meetingID"));
                appointment.setStudentID(resultSet.getString("studentID"));
                appointment.setFacultyID(resultSet.getString("facultyID"));
                appointment.setStartTime(resultSet.getString("startTime"));
                appointment.setEndTime(resultSet.getString("endTime"));
                appointment.setDate(resultSet.getString("date"));
                appointment.setDescription(resultSet.getString("description"));
                appointment.setStatus("status");
                return appointment;
            }
        },key.getKey().toString());
    }

    private Term getTermInfo(String id) {
        final String getTermInfo = "Select * from term where termID = ?";
        return jdbcTemplate.queryForObject(getTermInfo, new RowMapper<Term>() {
            @Override
            public Term mapRow(ResultSet resultSet, int i) throws SQLException {
                Term term = new Term();
                term.setStartDate(resultSet.getString("startDate"));
                term.setEndDate(resultSet.getString("endDate"));
                term.setName(resultSet.getString("name"));
                term.setTermID(resultSet.getString("termID"));
                return term;
            }
        },id);
    }

    private Collection<OfficeHours> getDayOfWeekOfficeHours(String facultyID,String termID, String day) {
        //Nested query to get faculty availabilityID for that term and fetch the office hour start time and end time
        final String SQL_DAY_OF_WEEK = "Select startTime,endTime from officeHours where day = ? and availability in " +
                "(Select availabilityID from availability where facultyID = ? and hasOfficeHours = 1 and term = ?)";
        return jdbcTemplate.query(SQL_DAY_OF_WEEK, new RowMapper<OfficeHours>() {
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

    private boolean isValidRequest(String meetingStartTime, String meetingEndTime, String date, String facultyID, String termID, String day) {

        //convert string to localDate
        LocalDate meetingDate = LocalDate.parse(date);
        LocalTime startTime = LocalTime.parse(meetingStartTime);
        LocalTime endTime = LocalTime.parse(meetingEndTime);
        LocalTime currentTime = LocalTime.now(ZoneId.of("US/Eastern"));
        LocalDate currentDate = LocalDate.now(ZoneId.of("US/Eastern"));

        if(checkExistingMeeting(meetingDate,facultyID,meetingStartTime) > 0) return false;

        //check if meeting date is before current date
        if(meetingDate.isBefore(currentDate)) return false;

        if(meetingDate.isEqual(currentDate) && startTime.isBefore(currentTime)) return false;
        //Check if the faculty has office hours or not
        Integer hasOfficeHours = isFacultyOfficeHours(facultyID,termID);

        if(hasOfficeHours != 1) return false;

        Term term = getTermInfo(String.valueOf(termID));

        //get current term starting date and ending date
        LocalDate startDate = LocalDate.parse(term.getStartDate());
        LocalDate endDate = LocalDate.parse(term.getEndDate());


        if(startDate.isBefore(meetingDate) && endDate.isAfter(meetingDate)) {

            Collection<OfficeHours> officeHours = getDayOfWeekOfficeHours(facultyID,termID,day);
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
            if(!validTime) return false;
        }
        return false;
    }
}