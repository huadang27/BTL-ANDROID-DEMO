package vn.embosua.ltddquanlynhanvienversion4.DieuKien;

import android.provider.ContactsContract;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import vn.embosua.ltddquanlynhanvienversion4.Model.Staff;

public class CheckFormat {
    public CheckFormat() {
    }

    private FormatCallBack aDefault = FormatCallBack.DEFAULT;

    public void addCallBack (FormatCallBack aDefault){
        this.aDefault = aDefault;
    }

    public boolean checkEmail(String email, String check){
        if (checkData(email)){
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                aDefault.FormatTrue(check);
                return true;
            }else{
                aDefault.FormatFail("wrong email format",check);
                return false;
            }
        }else{// khong bo trong
            aDefault.FormatFail("don't leave it blank",check);
            return false;
        }
    }

    public boolean checkPassword(String password, String check){
        if (checkData(password)){
            if (PASSWORD_PATTERN.matcher(password).matches()){
                aDefault.FormatTrue(check);
                return true;
            }else{
                aDefault.FormatFail("characters inside {[0-9], [a-z], [A-Z], [@#$%^&]}" +
                        "\nand must be more than 6 digits",check);
                return false;
            }
        }else{// khong bo trong
            aDefault.FormatFail("don't leave it blank",check);
            return false;
        }
    }

    public boolean checkConfirmPasswords(String password, String confirmPassword, String check){
        if (checkData(confirmPassword)){
            if (password.equals(confirmPassword)){
                aDefault.FormatTrue(check);
                return true;
            }
            else {
                aDefault.FormatFail("Passwords didn't match",check);
                return false;
            }
        }else{// khong bo trong
            aDefault.FormatFail("don't leave it blank",check);
            return false;
        }
    }

    public int getAge(String dateStart){
        int age = 0;
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateStart);
            Long ageInMillis = new Date().getTime() - date.getTime();
            age = new Date(ageInMillis).getYear()-70;
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("Date",""+e.toString());
        }
        return age;
    }

    public boolean checkBirthDay(String bithday, String check){
        if (checkData(bithday)){
            if (getAge(bithday)>=18 && getAge(bithday)<=70){
                aDefault.FormatTrue(check);
                return true;
            }
            else {
                aDefault.FormatFail("Invalid age",check);
                return false;
            }
        }else{// khong bo trong
            aDefault.FormatFail("don't leave it blank",check);
            return false;
        }
    }

    public Boolean checkStringInList(List<String> list, String string, String check){
        if (list.size()==0){
            aDefault.FormatFail("Error loading data: list null",check);
            return false;
        }

        if (checkDataNull(string,check)) {

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals(string)) {
                    aDefault.FormatTrue(check);
                    return true;
                }
            }

            aDefault.FormatFail("The entered string does not exist",check);
            return false;

        }else
            return false;
    }

    public Boolean checkPhone(String phone, String check){
        if (checkDataNull(phone,check))
            if (phone.matches("^[0-9]*$"))
                if (phone.length()==10){
                    aDefault.FormatTrue(check);
                    return true;
                }else {
                    aDefault.FormatFail("10 digit phone number",check);
                    return false;
                }
            else {
                aDefault.FormatFail("wrong format",check);
                return false;
            }
        else
            return false;
    }

    public boolean checkDataNull(String data, String check){
        if (checkData(data)){
            aDefault.FormatTrue(check);
            return true;
        }else{// khong bo trong
            aDefault.FormatFail("don't leave it blank",check);
            return false;
        }
    }

    private boolean checkData(String abc){
        if (abc.isEmpty() || abc == null){
            return false;
        }else{
            return true;
        }
    }

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^"
                    +"(?=.*[0-9])"          // ít nhất một chữ số
                    +"(?=.*[a-z])"          // ít nhất một chữ thường
                    +"(?=.*[A-Z])"          // ít nhất một chữ hoa
                    +"(?=.*[@#$%^&])"       // ít nhất một ký tự đặc biệt
                    +"(?=\\S+$)"            // không được có khoảng trắng
                    +".{6,}$"               // ít nhất 6 ký tự, (sau dấu ',' số ký tự lớn nhất có thể)
            );

///////////////////////////////////////////////////
    public boolean checkLogin(String email, String checkEmail, String password, String checkPass){
        Boolean t1 = checkDataNull(password,checkPass);
        Boolean t2 = checkEmail(email,checkEmail);
        if (t1 && t2)
            return true;
        else
            return false;
    }

    public boolean checkVercodeAndPass(String vercode,String check1,
                                       String password, String check2,
                                       String confirmpassword, String check3){
        Boolean t1 = checkDataNull(vercode, check1);
        Boolean t2 = checkPassword(password, check2);
        Boolean t3 = checkConfirmPasswords(password,confirmpassword, check3);

        if (t1 && t2 && t3)
            return true;
        else
            return false;
    }

    public boolean checkPorsonInfor(String photo, String check1,
                                    String name, String check2,
                                    String birthday, String check3,
                                    String cmt, String check4,
                                    String address, String check5){

        Boolean t1 = checkDataNull(photo, check1);
        Boolean t2 = checkDataNull(name, check2);
        Boolean t3 = checkBirthDay(birthday, check3);
        Boolean t4 = checkDataNull(cmt, check4);
        Boolean t5 = checkDataNull(address, check5);

        if (t1 && t2 && t3 && t4 && t5)
            return true;
        else
            return false;
    }

    public Boolean checkStaffInfor(String startdate, String check1,
                                   List<String> list2, String position, String check2,
                                   List<String> list3, String coeff, String check3,
                                   List<String> list4, String room, String check4){

        Boolean t1 = checkDataNull(startdate,check1);
        Boolean t2 = checkStringInList(list2,position,check2);
        Boolean t3 = checkStringInList(list3,coeff,check3);
        Boolean t4 = checkStringInList(list4,room,check4);

        if (t1 && t2 && t3 && t4)
            return true;
        else
            return false;
    }

    public Boolean checkContactInfor(String email, String check1,
                                     String phone, String check2){

        Boolean t2 = checkPhone(phone,check2);
        Boolean t1 = checkEmail(email,check1);

        if (t1 && t2)
            return true;
        else
            return false;
    }
    //String[] check = {PHOTO,NAME,BIRTHDAY,CMT,ADDRESS,PHONE,POSITION,COEFFIC,ROOM};
    public Boolean checkEditInfor(Staff staff, String[] check, List<String> list1, List<String> list2, List<String> list3){
        Boolean t1 = checkPorsonInfor(staff.getPhoto(),check[0],staff.getFullName(),check[1],
                staff.getBirthDay(),check[2],staff.getCMT(),check[3],staff.getAddress(),check[4]);
        Boolean t2 = checkPhone(staff.getPhoneNumber(),check[5]);
        Boolean t3 = checkStringInList(list1,staff.getPosition(),check[6]);
        Boolean t5 = checkStringInList(list2,String.valueOf(staff.getCoeffic()),check[7]);
        Boolean t4 = checkStringInList(list3,staff.getRoom(),check[8]);

        if (t1 && t2 && t3 && t4 && t5)
            return true;
        else
            return false;
    }

//    public Boolean Equals(Staff staff1, Staff staff2){
//
//    }
}


// So sánh 2 ngày
//if(date1.compareTo(date2)>0){
//                System.out.println("Date1 is after Date2");
//            }else if(date1.compareTo(date2)<0){
//                System.out.println("Date1 is before Date2");
//            }else{
//                System.out.println("Date1 is equal to Date2");
//            }
