package vn.embosua.ltddquanlynhanvienversion4.TaoTaiKhoan;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import vn.embosua.ltddquanlynhanvienversion4.DieuKien.CheckFormat;
import vn.embosua.ltddquanlynhanvienversion4.DieuKien.FormatCallBack;
import vn.embosua.ltddquanlynhanvienversion4.Model.PortraitActivity;
import vn.embosua.ltddquanlynhanvienversion4.Model.Staff;
import vn.embosua.ltddquanlynhanvienversion4.R;

public class ContactInfoActivity extends PortraitActivity implements FormatCallBack {

    static final int FINISH_ACTIVITY = 1001;
    static final int RESULT_DATA_STAFF = 2002;

    TextInputLayout tilPhone, tilEmail;
    ImageView imgBack;
    Button btNext;
    EditText edtEmail, edtPhoneNumber;

    Staff staff;
    String uPhone, uEmail;

    CheckFormat checkFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        getLinkViews();

        getDataStaff();

        getControls();
    }

    // nhận thông tin nhân viên đã nhập từ bên person infor
    private void getDataStaff() {
        Intent intent = getIntent();
        staff = (Staff) intent.getSerializableExtra("staff");
//        uPhone = staff.getPhoneNumber();
//        uEmail = staff.getEmail();
//        edtPhoneNumber.setText(uPhone);
//        edtEmail.setText(uEmail);
    }

    private void getControls() {
        // tiếp tục
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromView();
                checkFormat.addCallBack(ContactInfoActivity.this);
                if (checkFormat.checkContactInfor(uEmail,EMAIL,uPhone,PHONE)){
                    setDataStaff();
                    Intent intent = new Intent(ContactInfoActivity.this,CreatePassActivity.class);
                    intent.putExtra("staff",staff);
                    resultLauncher.launch(intent);
                }

            }
        });

        // thoát
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

    // set dư liệu cho nhân viên từ các giá trị lấy được trên view
    private void setDataStaff() {
        staff.setPhoneNumber(uPhone);
        staff.setEmail(uEmail);
    }

    // kiểm tra dữ liệu có bỏ trống không
    private boolean checkDataInfor(){
        if (checkData(uEmail) || checkData(uPhone)){
            Toast.makeText(ContactInfoActivity.this, "Not be empty", Toast.LENGTH_SHORT).show();
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
        uPhone = edtPhoneNumber.getText().toString().trim();
        uEmail = edtEmail.getText().toString().trim();
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
        tilPhone = findViewById(R.id.til_phone);
        tilEmail = findViewById(R.id.til_email);
        imgBack = findViewById(R.id.img_back);
        btNext = findViewById(R.id.bt_next);
        edtEmail = findViewById(R.id.edt_email);
        edtPhoneNumber = findViewById(R.id.edt_phone_number);
        staff = new Staff();
        checkFormat = new CheckFormat();
    }


    // ẩn bàn phím
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }

    @Override
    public void FormatTrue(String check) {
        switch (check){
            case PHONE: tilPhone.setHelperText(null); break;
            case EMAIL: tilEmail.setHelperText(null); break;
        }
    }

    @Override
    public void FormatFail(String messenger, String check) {
        switch (check){
            case PHONE: tilPhone.setHelperText(messenger); break;
            case EMAIL: tilEmail.setHelperText(messenger); break;
        }
    }
}