package com.hqdang.baitaplonversion1.TaoTaiKhoan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.hqdang.baitaplonversion1.Model.Staff;
import com.hqdang.baitaplonversion1.R;

public class StaffInfoActivity extends AppCompatActivity {
    static final int FINISH_ACTIVITY = 1001;
    static final int RESULT_DATA_STAFF = 2002;

    ImageView imgBack;
    EditText edtStartDay;
    AutoCompleteTextView actwPosition, acwtCoeffic, actwRoom;
    RadioGroup radioGroup;
    RadioButton radioAdmin, radioUser;
    Button btNext;
    Calendar calendar;
    List<String> listChuVu, listPhong, listHeSoLuong;

    String uStartDate, uPosition, uRoom;
    Float  uCoeffic;
    Boolean uAccesRight;
    Staff staff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_info);

        getLinkViews();

        getArrayAdapters();

        getDataStaff();

        getControls();
    }
// nhận thông tin người dùng từ trang PersonInfor
    private void getDataStaff() {
        Intent intent = getIntent();
        staff = (Staff) intent.getSerializableExtra("staff");
//        uStartDate = staff.getStartDate();
//        uPosition = staff.getPosition();
//        uCoeffic = staff.getCoeffic();
//        uRoom = staff.getRoom();
//        if (staff.getAccessRight() == null || staff.getAccessRight() == false)
//            uAccesRight = false;
//        else
//            uAccesRight = true;
//
//        edtStartDay.setText(uStartDate);
//        actwPosition.setText(uPosition);
//        acwtCoeffic.setText(String.valueOf(uCoeffic));
//        actwRoom.setText(uRoom);
//        if (uAccesRight && uAccesRight != null)
//            radioAdmin.setChecked(true);
//        else
//            radioUser.setChecked(true);
    }
 // nút back cho từng trang
    private void getControls() {

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getDataFromView();
//                setDataStaff();
//                Intent intent = new Intent();
//                intent.putExtra("staff",staff);
//                setResult(RESULT_DATA_STAFF,intent);
                finish();
            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromView();
                Toast.makeText(StaffInfoActivity.this, ""+uCoeffic, Toast.LENGTH_SHORT).show();
                if (checkDataInfor()){
                    setDataStaff();
                    Intent intent = new Intent(StaffInfoActivity.this,ContactInfoActivity.class);
                    intent.putExtra("staff",staff);
                    resultLauncher.launch(intent);
                }
            }
        });

        edtStartDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetChooseDate();
            }
        });
    }
 //thông tin lưu và chuyển sang ContactInfor
    private void setDataStaff() {
        staff.setAccessRight(uAccesRight);
        staff.setRoom(uRoom);
        staff.setCoeffic(uCoeffic);
        staff.setPosition(uPosition);
        staff.setStartDate(uStartDate);
    }

    private boolean checkDataInfor(){
        if (checkData(uPosition) || checkData(uRoom) || checkData(uStartDate) ){
            Toast.makeText(StaffInfoActivity.this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }
// checkDâtInfor gọi
    private boolean checkData(String abc){
        if (abc.isEmpty() || abc == null){
            return true;
        }else{
            return false;
        }
    }
    //lấy dữ liệu từ màn hình xuống
    private void getDataFromView() {
        uStartDate = edtStartDay.getText().toString().trim();
        uPosition = actwPosition.getText().toString().trim();
        uCoeffic = Float.valueOf(acwtCoeffic.getText().toString().trim());
        uRoom = actwRoom.getText().toString().trim();
        uAccesRight = getAccesRights();
    }

    // chek admin và user
    private Boolean getAccesRights() {
        int radioID = radioGroup.getCheckedRadioButtonId();
        if (radioID == R.id.radio_admin){
            return true;
        }else{
            return false;
        }
    }

//    private void setDataStaff() {
//        staff.setStartDate(uStartDate);
//        staff.setPosition(uPosition);
//        staff.setCoeffic(uCoeffic);
//        staff.setRoom(uRoom);
//        staff.setAccessRight(uAccesRight);
//    }
//
//    private void getDataFromView() {
//        uStartDate = edtStartDay.getText().toString().trim();
//        uPosition = actwPosition.getText().toString().trim();
//        uCoeffic = Float.valueOf(acwtCoeffic.getText().toString().trim());
//        uRoom = actwRoom.getText().toString().trim();
//        uAccesRight = getAccesRights();
//    }
//
//    private Boolean getAccesRights() {
//        int radioID = radioGroup.getCheckedRadioButtonId();
//        if (radioID == R.id.radio_admin){
//            return true;
//        }else{
//            return false;
//        }
//    }
// chờ tb trả về
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == FINISH_ACTIVITY){
                        setResult(FINISH_ACTIVITY);
                        finish();
                    }
                }
            });

    private void GetChooseDate(){
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        int mm = calendar.get(Calendar.MONTH);
        int yyyy = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Gán ngày tháng năm người dùng chọn.
                calendar.set(year,month,dayOfMonth);
                // Định dạng kiểu ngày tháng năm dd/MM/yyyy.
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                edtStartDay.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },yyyy,mm,dd);
        datePickerDialog.show();
    }

    private void getArrayAdapters() {
        ArrayAdapter<String> adapterChucVu = new ArrayAdapter<>(this,R.layout.drop_down_menu_text,listChuVu);
        actwPosition.setAdapter(adapterChucVu);

        ArrayAdapter<String> adapterPhong = new ArrayAdapter<>(this,R.layout.drop_down_menu_text,listPhong);
        actwRoom.setAdapter(adapterPhong);

        ArrayAdapter<String> adapterHeSoLuong = new ArrayAdapter<>(this,R.layout.drop_down_menu_text,listHeSoLuong);
        acwtCoeffic.setAdapter(adapterHeSoLuong);
    }

    private void getLinkViews() {
        imgBack = findViewById(R.id.img_back);
        edtStartDay = findViewById(R.id.edt_choose_date);
        actwPosition = findViewById(R.id.actw_position);
        acwtCoeffic = findViewById(R.id.actw_coeffic);
        actwRoom = findViewById(R.id.actw_room);
        radioGroup = findViewById(R.id.radio_group);
        radioAdmin = findViewById(R.id.radio_admin);
        radioUser = findViewById(R.id.radio_user);
        btNext = findViewById(R.id.bt_next);
        calendar = Calendar.getInstance();

        listChuVu = Arrays.asList(getResources().getStringArray(R.array.chuc_vu));
        listHeSoLuong = Arrays.asList(getResources().getStringArray(R.array.he_so_luong));
        //listPhong = Arrays.asList(getResources().getStringArray(R.array.phong));
        listPhong = new ArrayList<>();
        QueryListPhong();
        staff = new Staff();
    }

    public void QueryListPhong() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("dbRoom").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String value = dataSnapshot.getValue(String.class);
                    list.add(value);
                }
                listPhong.addAll(list);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}