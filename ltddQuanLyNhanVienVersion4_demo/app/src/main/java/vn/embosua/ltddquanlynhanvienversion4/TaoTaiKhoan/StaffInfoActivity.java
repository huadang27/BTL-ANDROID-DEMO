package vn.embosua.ltddquanlynhanvienversion4.TaoTaiKhoan;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
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

import vn.embosua.ltddquanlynhanvienversion4.DieuKien.CheckFormat;
import vn.embosua.ltddquanlynhanvienversion4.DieuKien.FormatCallBack;
import vn.embosua.ltddquanlynhanvienversion4.Model.PortraitActivity;
import vn.embosua.ltddquanlynhanvienversion4.Model.Staff;
import vn.embosua.ltddquanlynhanvienversion4.R;

public class StaffInfoActivity extends PortraitActivity implements FormatCallBack {
    static final int FINISH_ACTIVITY = 1001;
    static final int RESULT_DATA_STAFF = 2002;

    TextInputLayout tilStartDate, tilPosition, tilCoef, tilRoom;
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

    CheckFormat checkFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_info);

        getLinkViews();

        getArrayAdapters();

        getDataStaff();

        getControls();
    }

    // nhận thông tin nhân viên đã nhập từ bên person infor
    private void getDataStaff() {
        Intent intent = getIntent();
        staff = (Staff) intent.getSerializableExtra("staff");
    }

    private void getControls() {
        // nút thoát
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // nút tiếp tục
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromView();
                checkFormat.addCallBack(StaffInfoActivity.this);
                if (checkFormat.checkStaffInfor(uStartDate,STARDATE,
                        listChuVu,uPosition,POSITION,
                        listHeSoLuong,acwtCoeffic.getText().toString().trim(),COEFFIC,
                        listPhong,uRoom,ROOM)){
                    setDataStaff();
                    Intent intent = new Intent(StaffInfoActivity.this,ContactInfoActivity.class);
                    intent.putExtra("staff",staff);
                    resultLauncher.launch(intent);
                }
            }
        });

        // chọn ngày
        edtStartDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetChooseDate();
            }
        });
    }

    // set dư liệu cho nhân viên từ các giá trị lấy được trên view
    private void setDataStaff() {
        staff.setAccessRight(uAccesRight);
        staff.setRoom(uRoom);
        staff.setCoeffic(uCoeffic);
        staff.setPosition(uPosition);
        staff.setStartDate(uStartDate);
    }

    // kiểm tra dữ liệu có bỏ trống không
    private boolean checkDataInfor(){
        if (checkData(uPosition) || checkData(uRoom) || checkData(uStartDate) ){
            Toast.makeText(StaffInfoActivity.this, "Not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    private boolean checkData(String abc){
        if (abc.isEmpty() || abc == null){
            return true;
        }else{
            return false;
        }
    }

    // lấy dữ liệu trên view
    private void getDataFromView() {
        uStartDate = edtStartDay.getText().toString().trim();
        uPosition = actwPosition.getText().toString().trim();
        if (acwtCoeffic.getText().toString().trim().isEmpty()){
            uCoeffic = 0.0f;
        }else {
            uCoeffic = Float.valueOf(acwtCoeffic.getText().toString().trim());
        }
        uRoom = actwRoom.getText().toString().trim();
        uAccesRight = getAccesRights();
    }

    // kiểm tra quyền truy cập
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

    // lắng nghe kết quả trả về để đóng activity
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

    // open dialog để chọn ngày
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

    // set dư liệu list cho AutoCompleteTextView
    private void getArrayAdapters() {
        ArrayAdapter<String> adapterChucVu = new ArrayAdapter<>(this,R.layout.drop_down_menu_text,listChuVu);
        actwPosition.setAdapter(adapterChucVu);

        ArrayAdapter<String> adapterPhong = new ArrayAdapter<>(this,R.layout.drop_down_menu_text,listPhong);
        actwRoom.setAdapter(adapterPhong);

        ArrayAdapter<String> adapterHeSoLuong = new ArrayAdapter<>(this,R.layout.drop_down_menu_text,listHeSoLuong);
        acwtCoeffic.setAdapter(adapterHeSoLuong);
    }

    // liên kết các đối tượng hiển thị trên view
    private void getLinkViews() {
        tilStartDate = findViewById(R.id.til_choose_date);
        tilPosition = findViewById(R.id.til_position);
        tilCoef = findViewById(R.id.til_coef);
        tilRoom = findViewById(R.id.til_room);
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

        checkFormat = new CheckFormat();
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

    @Override
    public void FormatTrue(String check) {
        switch (check){
            case STARDATE: tilStartDate.setHelperText(null); break;
            case POSITION: tilPosition.setHelperText(null); break;
            case COEFFIC: tilCoef.setHelperText(null); break;
            case ROOM: tilRoom.setHelperText(null); break;
        }
    }

    @Override
    public void FormatFail(String messenger, String check) {
        switch (check){
            case STARDATE: tilStartDate.setHelperText(messenger); break;
            case POSITION: tilPosition.setHelperText(messenger); break;
            case COEFFIC: tilCoef.setHelperText(messenger); break;
            case ROOM: tilRoom.setHelperText(messenger); break;
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}