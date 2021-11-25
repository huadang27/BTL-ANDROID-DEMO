package com.hqdang.baitaplonversion1.ChiTiet;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.hqdang.baitaplonversion1.Model.ImageConvert;
import com.hqdang.baitaplonversion1.Model.Staff;
import com.hqdang.baitaplonversion1.R;

public class DetailStaff extends AppCompatActivity  {

    private static final int MY_PERMISSION_REQUEST_CODE_CALL_PHONE = 555;
    TextView id,name,birthday, gerden,email,phone,position,coeffic,startdate;
    Staff staff;
    ImageView imgBack, imgUser;
    Button bt_call,bt_mail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_staff);

        getLinkViews();

        getDataStaff();

        setDataViews();

        getControls();

     //---------------------------------------------------

        bt_call = this.findViewById(R.id.bt_call123);

        bt_call.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            askPermissionAndCall();
        }
    });
    bt_mail = this.findViewById(R.id.bt_mail123);

        bt_mail.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendMail();
        }

    });
    //-----------------------------------------------------
}

    //call
//------------------------------------------------------------------

    private void askPermissionAndCall() {

       // Xác nhận cho phép gọi
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // 23

            // check quyền
            int sendSmsPermisson = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE);

            if (sendSmsPermisson != PackageManager.PERMISSION_GRANTED) {
                // Nhắc nếu chưa cấp quyền
                this.requestPermissions(
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSION_REQUEST_CODE_CALL_PHONE
                );
                return;
            }
        }
        this.callNow();
    }

    @SuppressLint("MissingPermission")
    private void callNow() {
        String phoneNumber = this.phone.getText().toString();

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        try {
            this.startActivity(callIntent);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),"Cuộc gọi không thành công... " + ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    //------------------------------------------------------------
    //Mail
    private void sendMail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));

        intent.putExtra(Intent.EXTRA_EMAIL,new String[]{staff.getEmail()});
        intent.putExtra(Intent.EXTRA_SUBJECT,"Hello");
        intent.putExtra(Intent.EXTRA_TEXT," ");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }

       // dialog.show();

    //------------------------------------------------------------
    private void getControls() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        email.setText(staff.getEmail());
        phone.setText(staff.getPhoneNumber());
        position.setText(staff.getPosition());
        coeffic.setText(String.valueOf(staff.getCoeffic()));
        startdate.setText(staff.getStartDate());
    }

    private void getDataStaff() {
        if (getIntent().hasExtra("staff")){
            staff = (Staff) getIntent().getSerializableExtra("staff");
        }else {
            Toast.makeText(DetailStaff.this,"No Data.",Toast.LENGTH_SHORT).show();
        }
    }


    private void getLinkViews() {
        staff = new Staff();
        id = findViewById(R.id.txt_id);
        name = findViewById(R.id.txt_name);
        birthday = findViewById(R.id.txt_birthday);
        gerden = findViewById(R.id.txt_gender);
        email = findViewById(R.id.txt_email);
        phone = findViewById(R.id.txt_phone);
        position = findViewById(R.id.txt_positon);
        coeffic = findViewById(R.id.txt_coeffic);
        startdate = findViewById(R.id.txt_startdate);
        imgBack = findViewById(R.id.img_back );
        imgUser = findViewById(R.id.img_user_photo);
    }
}