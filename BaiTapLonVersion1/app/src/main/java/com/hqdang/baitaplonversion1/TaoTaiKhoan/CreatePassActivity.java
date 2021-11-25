package com.hqdang.baitaplonversion1.TaoTaiKhoan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.agconnect.auth.AGConnectAuth;

import java.util.List;

import com.hqdang.baitaplonversion1.DangNhap.SilentLoginActivity;
import com.hqdang.baitaplonversion1.DuLieu.AGCManager;
import com.hqdang.baitaplonversion1.DuLieu.FbManeger;
import com.hqdang.baitaplonversion1.Model.Staff;
import com.hqdang.baitaplonversion1.R;

import static com.huawei.agconnect.auth.VerifyCodeSettings.ACTION_REGISTER_LOGIN;

public class CreatePassActivity extends AppCompatActivity implements AGCManager.CreateAccountCallBack, AGCManager.SigInCallBack, FbManeger.FBCallBack {
    private String TAG = "CreatePassActivity";

    static final int FINISH_ACTIVITY = 1001;

    ImageView imgBack;
    EditText edtVerifeCode, edtPass, edtConfirmPass;
    Button btOK, btResendCode;

    String uPass, uConfirPass, uVercode, uEmail;
    Staff staff;

    AGCManager agcManager;
    FbManeger fbManeger;

    String uid, upass, uemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pass);

        getLinkViews();

        getDataStaff();

        agcManager.addCallBacks(CreatePassActivity.this);
        agcManager.sendConfirmCode(uEmail,ACTION_REGISTER_LOGIN);

        fbManeger.addCallBacks(CreatePassActivity.this);

        uid = AGConnectAuth.getInstance().getCurrentUser().getUid();
        fbManeger.QueryStaffInFo("dbUser",uid);

        getControls();
    }

    private void getDataStaff() {
        Intent intent = getIntent();
        staff = (Staff) intent.getSerializableExtra("staff");
        uEmail = staff.getEmail();
    }

    private void getControls() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dieu kien thanh cong
                // neu thanh cong thi finish() activity nay va cac activity phia truoc
                getDataFromView();
                if (checkDataInfor() && checkConfirPass()){
                    setDataStaff();

                    agcManager.creatWithEmail(uEmail, uVercode, uPass,"dbUser", staff);
                }

            }
        });

        btResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agcManager.sendConfirmCode(staff.getEmail(),ACTION_REGISTER_LOGIN);
            }
        });
    }

    private boolean checkConfirPass() {
        if (uPass.equals(uConfirPass))
            return true;
        else {
            Toast.makeText(CreatePassActivity.this, "Mat khau khong giong nhau !", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private void setDataStaff() {
        staff.setPassword(uPass);
        staff.setStaff(true);
    }

    private boolean checkDataInfor(){
        if (checkData(uConfirPass) || checkData(uPass) || checkData(uConfirPass)){
            Toast.makeText(CreatePassActivity.this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
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
        uVercode = edtVerifeCode.getText().toString().trim();
        uConfirPass = edtConfirmPass.getText().toString().trim();
        uPass = edtPass.getText().toString().trim();
    }

    private void getLinkViews() {
        imgBack = findViewById(R.id.img_back);
        edtVerifeCode = findViewById(R.id.edt_verife_code);
        edtPass = findViewById(R.id.edt_pass);
        edtConfirmPass = findViewById(R.id.edt_confirm_pass);
        btOK = findViewById(R.id.bt_ok);
        btResendCode = findViewById(R.id.bt_resend_code);
        staff = new Staff();
        agcManager = new AGCManager();
        fbManeger = new FbManeger();
    }

    @Override
    public void AfterSendSuccess() {
        Toast.makeText(this, "Đã gửi mã code vào Email.", Toast.LENGTH_SHORT).show();
        Log.e(TAG,"Đã gửi mã code vào Email.");
    }

    @Override
    public void AfterSendFail(String e) {
        Toast.makeText(this, "Lối khi gửi mã xác nhận:\n"+e, Toast.LENGTH_SHORT).show();
        Log.e(TAG,"Lối khi gửi mã xác nhận: "+e);
    }

    @Override
    public void AfterCreateAccountSuccess() {
        setResult(FINISH_ACTIVITY);
        Toast.makeText(CreatePassActivity.this, "Tao Thanh Cong", Toast.LENGTH_SHORT).show();
       //đăng xuất khỏi tài khoản vừa tạo
        AGConnectAuth.getInstance().signOut();
        //tài khoản admin tạo user không bị out
        agcManager.addSigInCallBack(CreatePassActivity.this);
        agcManager.signInWitEmailAndPassword(uemail,upass);

        //startActivity(new Intent(CreatePassActivity.this, SilentLoginActivity.class));
        //finish();
    }

    @Override
    public void AfterCreateAccountFail(String e) {
        Toast.makeText(this, "Lối khi tạo tài khoản:\n"+e, Toast.LENGTH_SHORT).show();
        Log.e(TAG,"Lối khi tạo tài khoản: "+e);
    }

    @Override
    public void ResetPassSuccess() {

    }

    @Override
    public void ResetPassFail(String e) {

    }

    @Override
    public void QueryStaffInFoSuccess(Staff staff) {
        upass = staff.getPassword();
        uemail = staff.getEmail();
    }

    @Override
    public void QueryStaffInFoFail(String e) {

    }

    @Override
    public void QueryCalenderSuccess(int off, int work, List<String> list) {

    }

    @Override
    public void SinginSuccess(String id) {
        finish();
    }

    @Override
    public void SinginFail(String e) {
        Toast.makeText(CreatePassActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(CreatePassActivity.this,SilentLoginActivity.class));
        finish();
    }
}