package vn.embosua.ltddquanlynhanvienversion4.TaoTaiKhoan;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import vn.embosua.ltddquanlynhanvienversion4.DieuKien.CheckFormat;
import vn.embosua.ltddquanlynhanvienversion4.DieuKien.FormatCallBack;
import vn.embosua.ltddquanlynhanvienversion4.Model.ImageConvert;
import vn.embosua.ltddquanlynhanvienversion4.Model.PortraitActivity;
import vn.embosua.ltddquanlynhanvienversion4.Model.Staff;
import vn.embosua.ltddquanlynhanvienversion4.R;

public class PersonInforActivity extends PortraitActivity implements FormatCallBack {
    String TAG = "PersonInfor";

    static final int FINISH_ACTIVITY = 1001;
    static final int RESULT_DATA_STAFF = 2002;
    final int REQUSE_CODE_CAMERA = 1231;

    TextInputLayout tilFullName, tilBirthDay, tilCMT, tilAddress;
    Button btNext, btChoosePhoto;
    RadioGroup radioGroup;
    RadioButton radioBoy, radioGirl;
    EditText edtName, edtBirthday, edtCMT, edtAddress;
    ImageView imgUserPhoto, imgBack;
    Calendar calendar;
    Bitmap bitmap;

    Staff staff;
    String uName, uPhoto, uBirthday, uCMT, uAddress;
    Boolean uGender;

    CheckFormat checkFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_infor);

        getLinkViews();

        getControls();

    }

    private void getControls() {
        // tiếp tục
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromView();
                checkFormat.addCallBack(PersonInforActivity.this);
                if (checkFormat.checkPorsonInfor(uPhoto,PHOTO,uName,NAME,uBirthday,BIRTHDAY,uCMT,CMT,uAddress,ADDRESS)){
                    setDataStaff();
                    Intent intent = new Intent(PersonInforActivity.this,StaffInfoActivity.class);
                    intent.putExtra("staff",staff);
                    resultLauncher.launch(intent);
                }
            }
        });

        // chụp ảnh
        btChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(PersonInforActivity.this, new String[]{Manifest.permission.CAMERA}, REQUSE_CODE_CAMERA);
            }
        });

        //chọn ảnh
        imgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(PersonInforActivity.this, new String[]{Manifest.permission.CAMERA}, REQUSE_CODE_CAMERA);
            }
        });

        // thoát
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // chọn ngày
        edtBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                GetChooseDate();
            }
        });
    }

    // set dư liệu cho nhân viên từ các giá trị lấy được trên view
    private void setDataStaff() {
        staff.setFullName(uName);
        staff.setBirthDay(uBirthday);
        staff.setGender(uGender);
        staff.setCMT(uCMT);
        staff.setAddress(uAddress);
        staff.setPhoto(uPhoto);
    }

    // lấy dữ liệu trên view
    private void getDataFromView() {
        uName = edtName.getText().toString().trim();
        uBirthday = edtBirthday.getText().toString().trim();
        uCMT = edtCMT.getText().toString().trim();
        uAddress = edtAddress.getText().toString().trim();
        uPhoto = getPhoto();
        uGender = getGender();
        checkFormat = new CheckFormat();
    }

    // kiểm tra giới tính
    private Boolean getGender() {
        int radioID = radioGroup.getCheckedRadioButtonId();
        if (radioID == R.id.radio_boy){
            return true;
        }else{
            return false;
        }
    }

    // chuyển ảnh thành string
    private String getPhoto() {
        if (bitmap == null){
            return "";
        }else {
            return new ImageConvert().toString(bitmap);
        }
    }

    // kết quả cấp quyền trả về
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == REQUSE_CODE_CAMERA) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            resultLauncherCamera.launch(intent);
        }else {
            Toast.makeText(PersonInforActivity.this, "You need camera permission", Toast.LENGTH_SHORT).show();
        }
    }

    // kết quả trả về khi chụp ảnh xong
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

    // kiểm tra dữ liệu có bỏ trống không
    private boolean checkDataInfor(){
        if (checkData(uName) || checkData(uBirthday) || checkData(uCMT)
                || checkData(uAddress) || checkData(uPhoto)){
            Toast.makeText(PersonInforActivity.this, "Not be empty", Toast.LENGTH_SHORT).show();
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
                edtBirthday.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },yyyy,mm,dd);
        datePickerDialog.show();
    }

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
//                    else if (result.getResultCode() == RESULT_DATA_STAFF){
//                        staff = (Staff) result.getData().getExtras().get("staff");
//                    }
                }
            });

    // liên các đối tượng hiển thị trên view
    private void getLinkViews() {
        tilFullName = findViewById(R.id.til_full_name);
        tilBirthDay = findViewById(R.id.til_birthday);
        tilCMT = findViewById(R.id.til_cmt);
        tilAddress = findViewById(R.id.til_address);
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

    // ẩn bàn phím khi nhấn ngoài mành hình
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        //getCurrentFocus()
    }

    @Override
    public void FormatTrue(String check) {
        switch (check){
            case PHOTO: btChoosePhoto.setTextColor(Color.parseColor("#000000")); break;
            case NAME: tilFullName.setHelperText(null); break;
            case BIRTHDAY: tilBirthDay.setHelperText(null); break;
            case CMT: tilCMT.setHelperText(null); break;
            case ADDRESS: tilAddress.setHelperText(null); break;
        }
    }

    @Override
    public void FormatFail(String messenger, String check) {
        switch (check){
            case PHOTO: btChoosePhoto.setTextColor(Color.parseColor("#F44336")); break;
            case NAME: tilFullName.setHelperText(messenger); break;
            case BIRTHDAY: tilBirthDay.setHelperText(messenger); break;
            case CMT: tilCMT.setHelperText(messenger); break;
            case ADDRESS: tilAddress.setHelperText(messenger); break;
        }
    }
}