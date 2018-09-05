package com.appointmentBookingService.Dao;

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
import java.util.Collection;
import java.util.List;

/**
 * Created by mourya on 8/21/18.
 */
@Repository("mysql")
public class StudentDaoImpl implements StudentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class FacultyRowMapper implements RowMapper<Faculty> {
        @Override
        public Faculty mapRow(ResultSet resultSet, int i) throws SQLException {
            Faculty faculty = new Faculty();
            faculty.setFacultyId(resultSet.getString("facultyID"));
            faculty.setName(resultSet.getString("name"));
            faculty.setEmail(resultSet.getString("email"));
            faculty.setDepartmentName(getDepartmentName(resultSet.getString("department")));
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

    private String getDepartmentName(String departmentID) {
        final String getDeptName = "SELECT name FROM department WHERE departmentID = ?";
        return jdbcTemplate.queryForObject(getDeptName, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("name");
            }
        }, departmentID);
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
        final String getFacultyAvailability = "SELECT hasOfficeHours FROM availability where facultyID = ? and term = ?";
        return jdbcTemplate.queryForObject(getFacultyAvailability, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getInt("hasOfficeHours");
            }
        }, facultyID, termID);
    }

    @Override
    public Appointment getStudentAppointments(int id) {
        final String getStudentAllAppointments = "Select * from appointment where studentID = ?";
        return jdbcTemplate.queryForObject(getStudentAllAppointments, new RowMapper<Appointment>() {
            @Override
            public Appointment mapRow(ResultSet resultSet, int i) throws SQLException {
                Appointment appointment = new Appointment();
                appointment.setMeetingID(resultSet.getString("meetingID"));
                appointment.setDescription(resultSet.getString("description"));
                appointment.setFacultyID(resultSet.getString("facultyID"));
                appointment.setStartTime(resultSet.getString("startTime"));
                appointment.setEndTime(resultSet.getString("endTime"));
                return appointment;
            }
        });
    }

    @Override
    public Student getStudentInfo(String id) {
        final String getStudentInfo = "Select * from student where universityID = ?";
        return jdbcTemplate.queryForObject(getStudentInfo, new RowMapper<Student>() {
            @Override
            public Student mapRow(ResultSet resultSet, int i) throws SQLException {
                Student student = new Student();
                student.setDepartmentName(getDepartmentName(resultSet.getString("department")));
                student.setEmail(resultSet.getString("email"));
                student.setName(resultSet.getString("name"));
                student.setUniversityID(resultSet.getString("universityID"));
                return student;
            }
        }, id);
    }

    @Override
    public Appointment requestAppointment(String startTime, String endTime,String facultyID, String studentID, String date, String description) {
        return addNewMeetingRequest(startTime, endTime, facultyID, studentID, date, description);
    }

    @Override
    public Term getTermInfo(String id) {
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

    private Collection<OfficeHours> getOfficeHours(String facultyID, String termID) {
        final String getOfficeHours = "Select * from officeHours where availability in " +
                "(Select availabilityID from availability where facultyID = ? and hasOfficeHours = 1 and term = ?)";
        return jdbcTemplate.query(getOfficeHours, new RowMapper<OfficeHours>() {
            @Override
            public OfficeHours mapRow(ResultSet resultSet, int i) throws SQLException {
                OfficeHours officeHours = new OfficeHours();
                officeHours.setAvailabilityID(resultSet.getString("availability"));
                officeHours.setDay(resultSet.getString("day"));
                officeHours.setStartTime(resultSet.getString("startTime"));
                officeHours.setEndTime(resultSet.getString("endTime"));
                return officeHours;
            }
        },facultyID,termID);
    }

    private Collection<OfficeHours> getDayOfWeekOfficeHours(String facultyID,String termID, String day) {
        final String getDayOfWeekOfficeHours = "Select startTime,endTime from officeHours where day = ? and availability in " +
                "(Select availabilityID from availability where facultyID = ? and hasOfficeHours = 1 and term = ?)";
        return jdbcTemplate.query(getDayOfWeekOfficeHours, new RowMapper<OfficeHours>() {
            @Override
            public OfficeHours mapRow(ResultSet resultSet, int i) throws SQLException {
                OfficeHours officeHours = new OfficeHours();
                officeHours.setStartTime(resultSet.getString("startTime"));
                officeHours.setEndTime(resultSet.getString("endTime"));
                return officeHours;
            }
        },day,facultyID,termID);
    }

    private String getExistingMeeting(LocalDate date,String facultyID, String startTime) {
        final String existingMeeting = "Select meetingID from appointment where startTime = ? and facultyID = ? and date = ?";
        return jdbcTemplate.queryForObject(existingMeeting, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("meetingID");
            }
        },startTime,facultyID,date);
    }

    private Appointment addNewMeetingRequest(String startTime, String endTime, String facultyID, String studentID, String date, String description) {
        KeyHolder key = new GeneratedKeyHolder();
        final String status = "pending";
        final String SQL_GET_APPOINTMENT = "Select * from appointment where meetingID = ?";
        jdbcTemplate.update(new PreparedStatementCreator() {
                                final String addNewMeetingRequest = "Insert into appointment (studentID,facultyID,startTime,endTime,date,description,status) Values " +
                                        "(?,?,?,?,?,?,?,?)";
                                @Override
                                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                                    final PreparedStatement ps = connection.prepareStatement(addNewMeetingRequest, Statement.RETURN_GENERATED_KEYS);
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
                appointment.setStudentID(resultSet.getString("studentID"));
                appointment.setFacultyID(resultSet.getString("facultyID"));
                appointment.setStartTime(resultSet.getString("startTime"));
                appointment.setEndTime(resultSet.getString("endTime"));
                appointment.setDate(resultSet.getString("date"));
                appointment.setDescription(resultSet.getString("description"));
                appointment.setAccepted("status");
                return appointment;
            }
        },key.getKey().intValue());
    }

    private boolean validateRequest(String meetingStartTime, String meetingEndTime, String date, String facultyID, String termID) {
        LocalDate meetingDate = LocalDate.parse(date);
        if(LocalDate.now().isAfter(meetingDate)) return false;
        Availability facultyAvailability = getFacultyAvailability(10);
        String termEndDate;
        String officeHourStart;
        String officeHourEnd;
        //TEST THIS
        Integer hasOfficeHours = isFacultyOfficeHours(facultyID,termID);

        if(hasOfficeHours != 1) return false;
        //TEST THIS
        Term term = getTermInfo(String.valueOf(termID));

        LocalDate startDate = LocalDate.parse(term.getStartDate());
        LocalDate endDate = LocalDate.parse(term.getEndDate());

       if(startDate.isBefore(meetingDate) && endDate.isAfter(meetingDate)) {
           getDayOfWeekOfficeHours(meetingDate.getDayOfWeek().toString(),facultyID,termID);
           getExistingMeeting(meetingDate,facultyID,meetingStartTime);
       }
        return false;
    }
}