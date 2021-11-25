package com.hqdang.baitaplonversion1.TaoTaiKhoan;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.hqdang.baitaplonversion1.Model.ImageConvert;
import com.hqdang.baitaplonversion1.Model.Staff;
import com.hqdang.baitaplonversion1.R;

public class PersonInforActivity extends AppCompatActivity {
    String TAG = "PersonInfor";

    static final int FINISH_ACTIVITY = 1001;
    static final int RESULT_DATA_STAFF = 2002;
    final int REQUSE_CODE_CAMERA = 1231;

    Button btNext, btChoosePhoto;
    RadioGroup radioGroup;
    RadioButton radioBoy, radioGirl;
    EditText edtName, edtBirthday, edtCMT, edtAddress;
    ImageView imgUserPhoto, imgBack;
    //Ngày tháng năm
    Calendar calendar;
    //chuyển ảnh
    Bitmap bitmap;

    Staff staff;
    String uName, uPhoto, uBirthday, uCMT, uAddress;
    Boolean uGender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_infor);

        getLinkViews();

        getControls();

    }

    private void getControls() {
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //lấy dữ liệu từ màn hình xuống
                getDataFromView();
                // kiểm tra có nhập đủ không
                if (checkDataInfor()){
                    setDataStaff();
                    Intent intent = new Intent(PersonInforActivity.this,StaffInfoActivity.class);
                    intent.putExtra("staff",staff);
                    resultLauncher.launch(intent);
                }
            }
        });

        btChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(PersonInforActivity.this, "Chon anh", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(PersonInforActivity.this, new String[]{Manifest.permission.CAMERA}, REQUSE_CODE_CAMERA);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
   //chọn ngày
        edtBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetChooseDate();
            }
        });
    }
// thông tin lưu và chuyển sang StafInforActivity
    private void setDataStaff() {
        staff.setFullName(uName);
        staff.setBirthDay(uBirthday);
        staff.setGender(uGender);
        staff.setCMT(uCMT);
        staff.setAddress(uAddress);
        staff.setPhoto(uPhoto);
    }
//lấy dữ liệu từ màn hình xuống
    private void getDataFromView() {
        uName = edtName.getText().toString().trim();
        uBirthday = edtBirthday.getText().toString().trim();
        uCMT = edtCMT.getText().toString().trim();
        uAddress = edtAddress.getText().toString().trim();
        uPhoto = getPhoto();
        uGender = getGender();
    }

    private Boolean getGender() {
        int radioID = radioGroup.getCheckedRadioButtonId();
        if (radioID == R.id.radio_boy){
            return true;
        }else{
            return false;
        }
    }

    private String getPhoto() {
        if (bitmap == null){
            return "";
        }else {
            return new ImageConvert().toString(bitmap);
        }
    }
// cấp quyền và chụp ảnh
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == REQUSE_CODE_CAMERA) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            resultLauncherCamera.launch(intent);
        }else {
            Toast.makeText(PersonInforActivity.this, "Bạn cần cấp quyền máy ảnh", Toast.LENGTH_SHORT).show();
        }
    }
// nhận ảnh trả về
    ActivityResultLauncher<Intent> resultLauncherCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null){
                        bitmap = (Bitmap) result.getData().getExtras().get("data");
                        imgUserPhoto.setImageBitmap(bitmap);
                    }
                }
            });
// kiểm tra có nhập đủ không (toàn bộ )
    private boolean checkDataInfor(){
        if (checkData(uName) || checkData(uBirthday) || checkData(uCMT)
                || checkData(uAddress) || checkData(uPhoto)){
            Toast.makeText(PersonInforActivity.this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }
//kiểm tra có nhập đủ không (từng chỗ ) được checkDataInfor gọi
    private boolean checkData(String abc){
        if (abc.isEmpty() || abc == null){
            return true;
        }else{
            return false;
        }
    }

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
                edtBirthday.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },yyyy,mm,dd);
        datePickerDialog.show();
    }
// Đóng Activity khi tạo xong tk
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == FINISH_ACTIVITY){
                        setResult(FINISH_ACTIVITY);
                        finish();
                    }
//                    else if (result.getResultCode() == RESULT_DATA_STAFF){
//                        staff = (Staff) result.getData().getExtras().get("staff");
//                    }
                }
            });

    private void getLinkViews() {
        btNext = findViewById(R.id.bt_next);
        btChoosePhoto = findViewById(R.id.bt_choose_photo);
        radioGroup = findViewById(R.id.radio_group);
        radioBoy = findViewById(R.id.radio_boy);
        radioGirl = findViewById(R.id.radio_girl);
        edtName = findViewById(R.id.edt_name);
        edtBirthday = findViewById(R.id.edt_birthday);
        edtCMT = findViewById(R.id.edt_cmt);
        edtAddress = findViewById(R.id.edt_address);
        imgUserPhoto = findViewById(R.id.img_user_photo);
        imgBack = findViewById(R.id.img_back);
        calendar = Calendar.getInstance();
        staff = new Staff();;
    }
}