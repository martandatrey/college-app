package com.pimpmyapp.collegeapp.pojo;

/**
 * Created by marta on 17-Jul-17.
 */

public class UserPojo {
    String profileImage = "";
    String user_id = "";
    String name = "";
    String pass = "";
    String email = "";
    String rollNo = "";
    String phoneNo = "";
    String branch = "";
    String year = "";
    String sem = "";
    boolean admin = false;
    boolean sAdmin = false;
    boolean wantsTobeAdmin = false;
    boolean blocked = false;

    @Override
    public String toString() {
        return "UserPojo{" +
                "profileImage='" + profileImage + '\'' +
                ", user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                ", email='" + email + '\'' +
                ", rollNo='" + rollNo + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", branch='" + branch + '\'' +
                ", year='" + year + '\'' +
                ", sem='" + sem + '\'' +
                ", admin=" + admin +
                ", sAdmin=" + sAdmin +
                ", wantsTobeAdmin=" + wantsTobeAdmin +
                ", blocked=" + blocked +
                '}';
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public boolean isWantsTobeAdmin() {
        return wantsTobeAdmin;
    }

    public void setWantsTobeAdmin(boolean wantsTobeAdmin) {
        this.wantsTobeAdmin = wantsTobeAdmin;
    }

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
