package com.hqdang.baitaplonversion1.TaoTaiKhoan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.hqdang.baitaplonversion1.Model.Staff;
import com.hqdang.baitaplonversion1.R;

public class ContactInfoActivity extends AppCompatActivity {

    static final int FINISH_ACTIVITY = 1001;
    static final int RESULT_DATA_STAFF = 2002;

    ImageView imgBack;
    Button btNext;
    EditText edtEmail, edtPhoneNumber;

    Staff staff;
    String uPhone, uEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        getLinkViews();

        getDataStaff();

        getControls();
    }

    private void getDataStaff() {
        Intent intent = getIntent();
        staff = (Staff) intent.getSerializableExtra("staff");
//        uPhone = staff.getPhoneNumber();
//        uEmail = staff.getEmail();
//        edtPhoneNumber.setText(uPhone);
//        edtEmail.setText(uEmail);
    }

    private void getControls() {
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromView();
                if (checkDataInfor()){
                    setDataStaff();
                    Intent intent = new Intent(ContactInfoActivity.this,CreatePassActivity.class);
                    intent.putExtra("staff",staff);
                    resultLauncher.launch(intent);
                }

            }
        });

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
    }

    private void setDataStaff() {
        staff.setPhoneNumber(uPhone);
        staff.setEmail(uEmail);
    }

    private boolean checkDataInfor(){
        if (checkData(uEmail) || checkData(uPhone)){
            Toast.makeText(ContactInfoActivity.this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
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

    private void getDataFromView() {
        uPhone = edtPhoneNumber.getText().toString().trim();
        uEmail = edtEmail.getText().toString().trim();
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
//                    else if (result.getResultCode() == RESULT_DATA_STAFF){
//                        staff = (Staff) result.getData().getExtras().get("staff");
//                    }
                }
            });

    private void getLinkViews() {
        imgBack = findViewById(R.id.img_back);
        btNext = findViewById(R.id.bt_next);
        edtEmail = findViewById(R.id.edt_email);
        edtPhoneNumber = findViewById(R.id.edt_phone_number);
        staff = new Staff();
    }
}