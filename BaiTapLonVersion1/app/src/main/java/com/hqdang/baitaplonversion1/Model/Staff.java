package com.hqdang.baitaplonversion1.Model;

import java.io.Serializable;

public class Staff implements Serializable {
    private String id;

    private String FullName;

    private Boolean Gender;

    private String BirthDay;

    private String CMT;

    private String Address;

    private String Photo;

    private String PhoneNumber;

    private String Email;

    private String Position;

    private Float Coeffic;

    private String Room;

    private String Password;

    private Boolean AccessRight;

    private Boolean IsStaff;

    private String StartDate;

    public Staff(String id, String fullName, Boolean gender, String birthDay,
                 String CMT, String address, String photo, String phoneNumber,
                 String email, String position, Float coeffic, String room,
                 String password, Boolean accessRight, Boolean isStaff,
                 String startDate) {
        this.id = id;
        FullName = fullName;
        Gender = gender;
        BirthDay = birthDay;
        this.CMT = CMT;
        Address = address;
        Photo = photo;
        PhoneNumber = phoneNumber;
        Email = email;
        Position = position;
        Coeffic = coeffic;
        Room = room;
        Password = password;
        AccessRight = accessRight;
        IsStaff = isStaff;
        StartDate = startDate;
    }

    public Staff() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public Boolean getGender() {
        return Gender;
    }

    public void setGender(Boolean gender) {
        Gender = gender;
    }

    public String getBirthDay() {
        return BirthDay;
    }

    public void setBirthDay(String birthDay) {
        BirthDay = birthDay;
    }

    public String getCMT() {
        return CMT;
    }

    public void setCMT(String CMT) {
        this.CMT = CMT;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public Float getCoeffic() {
        return Coeffic;
    }

    public void setCoeffic(Float coeffic) {
        Coeffic = coeffic;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Boolean getAccessRight() {
        return AccessRight;
    }

    public void setAccessRight(Boolean accessRight) {
        AccessRight = accessRight;
    }

    public Boolean getStaff() {
        return IsStaff;
    }

    public void setStaff(Boolean staff) {
        IsStaff = staff;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", FullName='" + FullName + '\'' +
                ", Gender='" + Gender + '\'' +
                ", BirthDay='" + BirthDay + '\'' +
                ", CMT='" + CMT + '\'' +
                ", Address='" + Address + '\'' +
                ", Photo='" + Photo + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", Email='" + Email + '\'' +
                ", Position='" + Position + '\'' +
                ", Coeffic=" + Coeffic +
                ", Room='" + Room + '\'' +
                ", Password='" + Password + '\'' +
                ", AccessRight=" + AccessRight +
                ", IsStaff=" + IsStaff +
                ", StartDate='" + StartDate + '\'' +
                '}';
    }

}
