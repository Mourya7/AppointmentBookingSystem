package com.appointmentBookingService.Entity;

/**
 * Created by mourya on 8/22/18.
 */
public class Department {
    private String departmentID;
    private String name;
    private String address;

    public Department() {
    }

    public String getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(String departmentID) {
        this.departmentID = departmentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}


