package vn.embosua.ltddquanlynhanvienversion4.Model;

import androidx.annotation.Nullable;

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

    public void setDataStaff(Staff st){
        setId(st.getId());
        setFullName(st.getFullName());
        setGender(st.getGender());
        setBirthDay(st.getBirthDay());
        setCMT(st.getCMT());
        setAddress(st.getAddress());
        setPhoto(st.getPhoto());
        setPhoneNumber(st.getPhoneNumber());
        setEmail(st.getEmail());
        setPosition(st.getPosition());
        setCoeffic(st.getCoeffic());
        setRoom(st.getRoom());
        setPassword(st.getPassword());
        setAccessRight(st.getAccessRight());
        setStaff(st.getStaff());
        setStartDate(st.getStartDate());
    }
    //String id, String fullName, Boolean gender, String birthDay,
    //                 String CMT, String address, String photo, String phoneNumber,
    //                 String email, String position, Float coeffic, String room,
    //                 String password, Boolean accessRight, Boolean isStaff,
    //                 String startDate)

    public Boolean Equals(Object obj) {
        Staff st = (Staff) obj;

        Boolean t1 = id.equals(st.getId());
        Boolean t2 = FullName.equals(st.getFullName());
        Boolean t3 = false;
        if (Gender == true && st.getGender() == true ||
                Gender == false && st.getGender() == false)
            t3 = true;
        Boolean t4 = BirthDay.equals(st.getBirthDay());
        Boolean t5 = CMT.equals(st.getCMT());
        Boolean t6 = Address.equals(st.getAddress());
        Boolean t7 = Photo.equals(st.getPhoto());
        Boolean t8 = PhoneNumber.equals(st.getPhoneNumber());
        Boolean t9 = Email.equals(st.getEmail());
        Boolean t10 = Position.equals(st.getPosition());
        Boolean t11 = false;
        Float epsilon = Float.MIN_NORMAL;
        if (Math.abs((Coeffic - st.getCoeffic()))<epsilon)
            t11 = true;
        Boolean t12 = Room.equals(st.getRoom());
        Boolean t13 = Password.equals(st.getPassword());
        Boolean t14 = false;
        if (AccessRight == true && st.getAccessRight() == true ||
                AccessRight == false && st.getAccessRight() == false)
            t14 = true;
        Boolean t15 = false;
        if (IsStaff == true && st.getStaff() == true ||
                IsStaff == false && st.getStaff() == false)
            t15 = true;
        Boolean t16 = StartDate.equals(st.getStartDate());
        return t1 && t2 && t3 && t4 && t5 && t6 && t7 &&
                t8 && t9 && t10 && t11 && t12 && t13 &&
                t14 && t15 && t16;
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
