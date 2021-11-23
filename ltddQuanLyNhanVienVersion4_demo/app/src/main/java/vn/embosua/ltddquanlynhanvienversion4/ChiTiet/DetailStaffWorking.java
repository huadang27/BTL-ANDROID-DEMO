package vn.embosua.ltddquanlynhanvienversion4.ChiTiet;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.huawei.agconnect.auth.AGConnectAuth;

import java.text.SimpleDateFormat;
import java.util.Date;

import vn.embosua.ltddquanlynhanvienversion4.DanhSach.ListHistoryActivity;
import vn.embosua.ltddquanlynhanvienversion4.Model.EditHistory;
import vn.embosua.ltddquanlynhanvienversion4.Model.ImageConvert;
import vn.embosua.ltddquanlynhanvienversion4.Model.PortraitActivity;
import vn.embosua.ltddquanlynhanvienversion4.Model.Staff;
import vn.embosua.ltddquanlynhanvienversion4.Model.StaffDeleted;
import vn.embosua.ltddquanlynhanvienversion4.R;
import vn.embosua.ltddquanlynhanvienversion4.TinhCong.PayRollActivity;

public class DetailStaffWorking extends PortraitActivity {

    static final int FINISH_ACTIVITY = 1001;
    static final int MY_PERMISSION_REQUEST_CODE_CALL_PHONE = 1002;
    static final int MY_PERMISSION_REQUEST_CODE_SEND_SMS = 1003;

    DatabaseReference databaseReference;

    TextView id,name,birthday, gerden, cmt, address, email,phone,position,coeffic, room, admin, startdate;
    Staff staff;
    ImageView imgBack, imgUser;
    ImageButton IBEditInfor, IBDelete, IBFeedback, IBHistory, IBPayroll;

    String uID = AGConnectAuth.getInstance().getCurrentUser().getUid();
    Boolean working, editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_staff_working);

        getLinkViews();

        getDataStaff();

        setDataViews();

        getControls();

    }

    private void getControls() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        IBEditInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailStaffWorking.this, EditInforActivity.class);
                intent.putExtra("staff",staff);
                if (staff.getAccessRight()){
                    if (uID.equals("686229645124699520")){
                        resultLauncher.launch(intent);
                    }else {
                        Toast.makeText(DetailStaffWorking.this, "You do not have permission to edit infor admin", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    resultLauncher.launch(intent);
                }
            }
        });
        IBDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (staff.getAccessRight()){
                    if (uID.equals("686229645124699520")){
                        if (staff.getId().equals(uID)){
                            Toast.makeText(DetailStaffWorking.this, "You can't erase yourself", Toast.LENGTH_SHORT).show();
                        }else {
                            openDialogDelete();
                        }
                    }else {
                        Toast.makeText(DetailStaffWorking.this, "You do not have permission to delete admin", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    openDialogDelete();
                }
            }
        });

        IBFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogFeedBack();
            }
        });

        IBHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailStaffWorking.this,ListHistoryActivity.class);
                intent.putExtra("staff",staff);
                startActivity(intent);
            }
        });

        IBPayroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailStaffWorking.this, PayRollActivity.class);
                intent.putExtra("id",staff.getId());
                startActivity(intent);
            }
        });

        if (editor){
            IBDelete.setVisibility(View.GONE);
            IBEditInfor.setVisibility(View.GONE);
            IBHistory.setVisibility(View.GONE);
            IBPayroll.setVisibility(View.GONE);
        }
    }

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

    public void openDialogFeedBack(){
        final Dialog dialog = new Dialog(DetailStaffWorking.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_feedback);

        Window window = dialog.getWindow();
        if (window == null) return;

        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(true);

        ImageButton phone = dialog.findViewById(R.id.bt_phone);
        ImageButton sms = dialog.findViewById(R.id.bt_sms);
        ImageButton email = dialog.findViewById(R.id.bt_email);

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions(Manifest.permission.CALL_PHONE,MY_PERMISSION_REQUEST_CODE_CALL_PHONE);
            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions(Manifest.permission.SEND_SMS,MY_PERMISSION_REQUEST_CODE_SEND_SMS);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gmailNow();
            }
        });

        dialog.show();
    }

    // kiem tra quyen
    private void checkPermissions(String cp, int requestCode){
        // kiem tra phien ban android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(
                    new String[]{cp},
                    requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions == null || grantResults == null || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(DetailStaffWorking.this,"You need permission",Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == MY_PERMISSION_REQUEST_CODE_CALL_PHONE) {
            callNow();
        }

        if (requestCode == MY_PERMISSION_REQUEST_CODE_SEND_SMS) {
            smsNow();
        }
    }

    @SuppressLint("MissingPermission")
    private void callNow() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + staff.getPhoneNumber()));
        try {
            this.startActivity(callIntent);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),"Call Fail... " + ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    private void smsNow() {
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + staff.getPhoneNumber()));
        smsIntent.putExtra("sms_body", "AndOr Hello!\n");
        try {
            this.startActivity(smsIntent);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),"SMS Fail... " + ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void gmailNow(){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));

        intent.putExtra(Intent.EXTRA_EMAIL,new String[]{staff.getEmail()});
        intent.putExtra(Intent.EXTRA_SUBJECT,"Andor App Feedback");
        intent.putExtra(Intent.EXTRA_TEXT,"Hello");

        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }else {
            Toast.makeText(DetailStaffWorking.this, "No App is Installed", Toast.LENGTH_SHORT).show();
        }
    }

    public void openDialogDelete(){
        final Dialog dialog = new Dialog(DetailStaffWorking.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_staff_delete);

        Window window = dialog.getWindow();
        if (window == null) return;

        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(true);

        EditText edtReason = dialog.findViewById(R.id.edt_reason);
        TextInputLayout tilReason = dialog.findViewById(R.id.til_reason);

        Button btCancel = dialog.findViewById(R.id.bt_cancel);
        Button btDelete = dialog.findViewById(R.id.bt_delete);


        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reason = edtReason.getText().toString().trim();
                if (reason.isEmpty() || reason == null){
                    tilReason.setHelperText("Not be empty");
                }else {
                    // luu lich su
                    staff.setStaff(false);
                    dialog.dismiss();
                    saveHistory(reason);
                }
            }
        });
        dialog.show();
    }

    private void saveHistory(String reason) {
        // dinh dang thoi gian
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm");
        // tao lich su
        EditHistory history = new EditHistory();
        history.setDateTime(simpleDateFormat.format(new Date().getTime()));
        history.setIDStaff(staff.getId());
        history.setIDEditor(uID);
        history.setReason(reason);
        history.setInfoCorrected(staff);
        // luu lich su
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String random = databaseReference.child("dbHistory").push().getKey();
        databaseReference.child("dbHistory").child(random).setValue(history);

        databaseReference.child("dbUser").child(staff.getId()).setValue(staff);
        setResult(FINISH_ACTIVITY);
        Toast.makeText(DetailStaffWorking.this, "Success", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setDataViews() {
        imgUser.setImageBitmap(new ImageConvert().toBitmap(staff.getPhoto()));
        id.setText(staff.getId());
        name.setText(staff.getFullName());
        birthday.setText(staff.getBirthDay());
        if (staff.getGender()){
            gerden.setText("Man");
        }else {
            gerden.setText("Woman");
        }
        cmt.setText(staff.getCMT());
        address.setText(staff.getAddress());
        email.setText(staff.getEmail());
        phone.setText(staff.getPhoneNumber());
        position.setText(staff.getPosition());
        coeffic.setText(String.valueOf(staff.getCoeffic()));
        room.setText(staff.getRoom());
        if (staff.getAccessRight()){
            admin.setText("true");
        }else {
            admin.setText("false");
        }
        startdate.setText(staff.getStartDate());

    }

    private void getDataStaff() {
        if (getIntent().hasExtra("staff")){
            staff = (Staff) getIntent().getSerializableExtra("staff");
            working = getIntent().getBooleanExtra("working",true);
            editor = getIntent().getBooleanExtra("editor",false);
        }else {
            Toast.makeText(DetailStaffWorking.this,"No Data.",Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("WrongViewCast")
    private void getLinkViews() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        staff = new Staff();
        id = findViewById(R.id.txt_id);
        name = findViewById(R.id.txt_name);
        birthday = findViewById(R.id.txt_birthday);
        gerden = findViewById(R.id.txt_gender);
        cmt = findViewById(R.id.txt_cmt);
        address = findViewById(R.id.txt_address);
        email = findViewById(R.id.txt_email);
        phone = findViewById(R.id.txt_phone);
        position = findViewById(R.id.txt_positon);
        coeffic = findViewById(R.id.txt_coeffic);
        room = findViewById(R.id.txt_room);
        admin = findViewById(R.id.txt_admin);
        startdate = findViewById(R.id.txt_startdate);
        imgBack = findViewById(R.id.img_back );
        imgUser = findViewById(R.id.img_user_photo);
        IBEditInfor = findViewById(R.id.bt_edit_infor);
        IBDelete = findViewById(R.id.bt_delete_infor);
        IBFeedback = findViewById(R.id.bt_feedback);
        IBHistory = findViewById(R.id.bt_history);
        IBPayroll = findViewById(R.id.bt_payroll);
    }
}

//  xóa tài khoản cũ
// dinh dang thoi gian
//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm");
//                    // tao lich su delete
//                    staff.setStaff(false);
//                    StaffDeleted staffDeleted = new StaffDeleted(simpleDateFormat.format(new Date().getTime())
//                            ,AGConnectAuth.getInstance().getCurrentUser().getUid(),staff,reason);
//                    // luu lich su
//                    databaseReference.child("dbStaffDelete").child(staff.getId()).setValue(staffDeleted);
//                    //huy quyen nhan vien
//                    databaseReference.child("dbUser").child(staff.getId()).setValue(staff);
//                    dialog.dismiss();
//                    finish();