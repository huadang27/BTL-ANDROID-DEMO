package vn.embosua.ltddquanlynhanvienversion4.ChiTiet;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.huawei.agconnect.auth.AGConnectAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vn.embosua.ltddquanlynhanvienversion4.DangNhap.LogInActivity;
import vn.embosua.ltddquanlynhanvienversion4.DieuKien.CheckFormat;
import vn.embosua.ltddquanlynhanvienversion4.DieuKien.FormatCallBack;
import vn.embosua.ltddquanlynhanvienversion4.Model.EditHistory;
import vn.embosua.ltddquanlynhanvienversion4.Model.ImageConvert;
import vn.embosua.ltddquanlynhanvienversion4.Model.PortraitActivity;
import vn.embosua.ltddquanlynhanvienversion4.Model.Staff;
import vn.embosua.ltddquanlynhanvienversion4.R;
import vn.embosua.ltddquanlynhanvienversion4.TaoTaiKhoan.PersonInforActivity;

public class EditInforActivity extends PortraitActivity implements FormatCallBack {

    final int REQUSE_CODE_CAMERA = 1231;
    static final int FINISH_ACTIVITY = 1001;

    Staff staff, staffNew;
    ImageView imgBack, imgUser;
    RadioGroup rgGender, rgAccessRight;
    RadioButton rdoMan, rdoWoman, rdoAdmin, rdoUser;
    TextInputLayout tilFullName, tilBirthDay, tilCMT, tilAddress, tilPhone, tilPosition, tilCoef, tilRoom;
    EditText edtName, edtBirthday, edtCMT, edtAddress, edtPhoneNumber;
    AutoCompleteTextView actwPosition, acwtCoeffic, actwRoom;
    Button btSave;

    String uPhoto, uName, uBirthday, uCMT, uAddress, uPhone, uPosition, uRoom;
    Boolean uGender, uAccesRight;
    Float  uCoeffic;

    List<String> listChuVu, listPhong, listHeSoLuong;
    String[] check = {PHOTO,NAME,BIRTHDAY,CMT,ADDRESS,PHONE,POSITION,COEFFIC,ROOM};

    Bitmap bitmap;

    Calendar calendar;
    CheckFormat checkFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_infor);

        getLinkViews();

        getDataStaff();

        setDataViews();

        getArrayAdapters();

        getControls();
    }

    private void getControls() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(EditInforActivity.this, new String[]{Manifest.permission.CAMERA}, REQUSE_CODE_CAMERA);
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromView();
                setDataStaff();
                checkFormat.addCallBack(EditInforActivity.this);
                //Do you really want to change?
                if (checkFormat.checkEditInfor(staffNew,check,listChuVu,listHeSoLuong,listPhong)){
                    if (!staff.Equals(staffNew)){
                        // luu lich su
                        saveHistory("edit01101000");

                    }else // thong tin chua duoc thay doi
                        Toast.makeText(EditInforActivity.this, "Information cannot be changed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        edtBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetChooseDate();
            }
        });
    }

    private void saveHistory(String reason) {
        // dinh dang thoi gian
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm");
        // tao lich su
        EditHistory history = new EditHistory();
        history.setDateTime(simpleDateFormat.format(new Date().getTime()));
        history.setIDStaff(staffNew.getId());
        history.setIDEditor(AGConnectAuth.getInstance().getCurrentUser().getUid());
        history.setReason(reason);
        history.setInfoCorrected(staffNew);
        // luu lich su
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String random = databaseReference.child("dbHistory").push().getKey();
        databaseReference.child("dbHistory").child(random).setValue(history);

        databaseReference.child("dbUser").child(staffNew.getId()).setValue(staffNew);
        setResult(FINISH_ACTIVITY);
        Toast.makeText(EditInforActivity.this, "Success", Toast.LENGTH_SHORT).show();
        finish();
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


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == REQUSE_CODE_CAMERA) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            resultLauncherCamera.launch(intent);
        }else {
            Toast.makeText(EditInforActivity.this, "You need camera permission", Toast.LENGTH_SHORT).show();
        }
    }

    ActivityResultLauncher<Intent> resultLauncherCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null){
                        bitmap = (Bitmap) result.getData().getExtras().get("data");
                        imgUser.setImageBitmap(bitmap);
                    }
                }
            });

    private void setDataStaff() {
        staffNew.setFullName(uName);
        staffNew.setBirthDay(uBirthday);
        staffNew.setCMT(uCMT);
        staffNew.setAddress(uAddress);
        staffNew.setPhoto(uPhoto);
        staffNew.setGender(uGender);
        staffNew.setPosition(uPosition);
        staffNew.setCoeffic(uCoeffic);
        staffNew.setRoom(uRoom);
        staffNew.setAccessRight(uAccesRight);
        staffNew.setPhoneNumber(uPhone);
    }

    private void getDataFromView() {
        uName = edtName.getText().toString().trim();
        uBirthday = edtBirthday.getText().toString().trim();
        uCMT = edtCMT.getText().toString().trim();
        uAddress = edtAddress.getText().toString().trim();
        uPhoto = getPhoto();
        uGender = getGender();

        uPosition = actwPosition.getText().toString().trim();
        if (acwtCoeffic.getText().toString().trim().isEmpty()){
            uCoeffic = 0.0f;
        }else {
            uCoeffic = Float.valueOf(acwtCoeffic.getText().toString().trim());
        }
        uRoom = actwRoom.getText().toString().trim();
        uAccesRight = getAccesRights();

        uPhone = edtPhoneNumber.getText().toString().trim();
    }

    private Boolean getAccesRights() {
        int radioID = rgAccessRight.getCheckedRadioButtonId();
        if (radioID == R.id.radio_admin){
            return true;
        }else{
            return false;
        }
    }

    private Boolean getGender() {
        int radioID = rgGender.getCheckedRadioButtonId();
        if (radioID == R.id.radio_boy){
            return true;
        }else{
            return false;
        }
    }

    private String getPhoto() {
        if (bitmap == null){
            return staff.getPhoto();
        }else {
            return new ImageConvert().toString(bitmap);
        }
    }

    private void setDataViews() {
        imgUser.setImageBitmap(new ImageConvert().toBitmap(staff.getPhoto()));
        edtName.setText(staff.getFullName());
        edtBirthday.setText(staff.getBirthDay());
        if (staff.getGender()){
            rdoMan.setChecked(true);
            rdoWoman.setChecked(false);
        }else {
            rdoMan.setChecked(false);
            rdoWoman.setChecked(true);
        }
        edtCMT.setText(staff.getCMT());
        edtAddress.setText(staff.getAddress());
        edtPhoneNumber.setText(staff.getPhoneNumber());

        // hien list actw
        actwPosition.setFreezesText(false);

        actwPosition.setText(staff.getPosition());
        acwtCoeffic.setText(String.valueOf(staff.getCoeffic()));
        actwRoom.setText(staff.getRoom());
        if (staff.getAccessRight()){
            rdoAdmin.setChecked(true);
            rdoUser.setChecked(false);
        }else {
            rdoAdmin.setChecked(false);
            rdoUser.setChecked(true);
        }
    }

    private void getDataStaff() {
        if (getIntent().hasExtra("staff")){
            staff = (Staff) getIntent().getSerializableExtra("staff");
            staffNew.setDataStaff(staff);
        }else {
            Toast.makeText(EditInforActivity.this,"Data Fail.",Toast.LENGTH_SHORT).show();
            finish();
        }
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
        staff = new Staff();
        staffNew = new Staff();

        //image
        imgBack = findViewById(R.id.img_back);
        imgUser = findViewById(R.id.img_user_photo);

        //text input layout
        tilFullName = findViewById(R.id.til_full_name);
        tilBirthDay = findViewById(R.id.til_birthday);
        tilCMT = findViewById(R.id.til_cmt);
        tilAddress = findViewById(R.id.til_address);
        tilPosition = findViewById(R.id.til_position);
        tilCoef = findViewById(R.id.til_coef);
        tilRoom = findViewById(R.id.til_room);
        tilPhone = findViewById(R.id.til_phone);

        //edt
        edtName = findViewById(R.id.edt_name);
        edtBirthday = findViewById(R.id.edt_birthday);
        edtCMT = findViewById(R.id.edt_cmt);
        edtAddress = findViewById(R.id.edt_address);
        edtPhoneNumber = findViewById(R.id.edt_phone_number);

        // AutoCompleteTextView
        actwPosition = findViewById(R.id.actw_position);
        acwtCoeffic = findViewById(R.id.actw_coeffic);
        actwRoom = findViewById(R.id.actw_room);

        //radio
        rgGender = findViewById(R.id.radio_group_gender);
        rdoMan = findViewById(R.id.radio_boy);
        rdoWoman = findViewById(R.id.radio_girl);
        rgAccessRight = findViewById(R.id.radio_group_access_rights);
        rdoAdmin = findViewById(R.id.radio_admin);
        rdoUser = findViewById(R.id.radio_user);

        // button
        btSave = findViewById(R.id.bt_save);

        // load data
        listChuVu = Arrays.asList(getResources().getStringArray(R.array.chuc_vu));
        listHeSoLuong = Arrays.asList(getResources().getStringArray(R.array.he_so_luong));
        listPhong = new ArrayList<>();
        QueryListPhong();

        checkFormat = new CheckFormat();

        calendar = Calendar.getInstance();
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

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    @Override
    public void FormatTrue(String check) {
        switch (check){
            case NAME: tilFullName.setHelperText(null); break;
            case BIRTHDAY: tilBirthDay.setHelperText(null); break;
            case CMT: tilCMT.setHelperText(null); break;
            case ADDRESS: tilAddress.setHelperText(null); break;

            case PHONE: tilPhone.setHelperText(null); break;

            case POSITION: tilPosition.setHelperText(null); break;
            case COEFFIC: tilCoef.setHelperText(null); break;
            case ROOM: tilRoom.setHelperText(null); break;
        }
    }

    @Override
    public void FormatFail(String messenger, String check) {
        switch (check){
            case NAME: tilFullName.setHelperText(messenger); break;
            case BIRTHDAY: tilBirthDay.setHelperText(messenger); break;
            case CMT: tilCMT.setHelperText(messenger); break;
            case ADDRESS: tilAddress.setHelperText(messenger); break;

            case PHONE: tilPhone.setHelperText(messenger); break;

            case POSITION: tilPosition.setHelperText(messenger); break;
            case COEFFIC: tilCoef.setHelperText(messenger); break;
            case ROOM: tilRoom.setHelperText(messenger); break;

            case PHOTO:
                Toast.makeText(this, "Anh loi", Toast.LENGTH_SHORT).show();
        }
    }
}

//    lưu lịch sử cũ
//                        // dinh dang thoi gian
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm");
//                        // tao lich su
//                        EditHistory history = new EditHistory(simpleDateFormat.format(new Date().getTime()),
//                                staffNew.getId(), AGConnectAuth.getInstance().getCurrentUser().getUid(),staffNew);
//                        // luu lich su
//                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//                        String a = databaseReference.child("dbHistory").push().getKey();
//                        databaseReference.child("dbHistory").child(a).setValue(history);
//
//                        databaseReference.child("dbHistory").child(a).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot snapshot) {
//                                if (snapshot.exists()){
//                                    Toast.makeText(EditInforActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                                    databaseReference.child("dbUser").child(staffNew.getId()).setValue(staffNew);
//                                    setResult(FINISH_ACTIVITY);
//                                    finish();
//                                }else {
//                                    Toast.makeText(EditInforActivity.this, "Can't connect to the server", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError error) {
//                                Toast.makeText(EditInforActivity.this, "Can't connect to the server", Toast.LENGTH_SHORT).show();
//                            }
//                        });
