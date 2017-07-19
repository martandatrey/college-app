package com.pimpmyapp.collegeapp.pojo;

/**
 * Created by marta on 17-Jul-17.
 */

public class UserPojo {
    String user_id;
    String name = "";
    String pass = "";
    String email = "";
    String rollNo = "";
    String phoneNo = "";
    String branch = "";
    String year = "";
    String sem = "";
    boolean admin = false;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean issAdmin() {
        return sAdmin;
    }

    public void setsAdmin(boolean sAdmin) {
        this.sAdmin = sAdmin;
    }

    boolean sAdmin = false;

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getYear() {

        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
