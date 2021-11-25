package com.hqdang.baitaplonversion1.QuenMatKhau;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.hqdang.baitaplonversion1.DuLieu.AGCManager;
import com.hqdang.baitaplonversion1.R;

import static com.huawei.agconnect.auth.VerifyCodeSettings.ACTION_RESET_PASSWORD;

public class ResetPassActivity extends AppCompatActivity implements AGCManager.CreateAccountCallBack {
    String TAG = "ResetPassActivity";
    static final int FINISH_ACTIVITY = 1001;

    Button btOK, btResendCode;
    EditText edtVerCode, edtPass, edtConfirPass;

    String uEmail, uVerCode, uPass, uConfirPass;

    AGCManager agcManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        getLinkViews();

        agcManager.addCallBacks(ResetPassActivity.this);

        Intent intent = getIntent();
        uEmail = intent.getStringExtra("email");

        agcManager.sendConfirmCode(uEmail,ACTION_RESET_PASSWORD);

        getControls();
    }

    private void getControls() {
        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromView();
                if (checkDataInfor() && checkConfirPass()){
                    //resultLauncher.launch(new Intent(ResetPassActivity.this,ResetPassSuccessActivity.class));
                    agcManager.resetPasswordWithEemail(uEmail,uPass,uVerCode);
                }

            }
        });

        btResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agcManager.sendConfirmCode(uEmail,ACTION_RESET_PASSWORD);
            }
        });
    }

    private boolean checkConfirPass() {
        if (uPass.equals(uConfirPass))
            return true;
        else {
            Toast.makeText(ResetPassActivity.this, "Mat khau khong giong nhau !", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean checkDataInfor(){
        if (checkData(uConfirPass) || checkData(uPass) || checkData(uConfirPass)){
            Toast.makeText(ResetPassActivity.this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
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
        uVerCode = edtVerCode.getText().toString().trim();
        uConfirPass = edtConfirPass.getText().toString().trim();
        uPass = edtPass.getText().toString().trim();
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == FINISH_ACTIVITY){
                        Intent intent = new Intent();
                        intent.putExtra("email",uEmail);
                        intent.putExtra("pass",uPass);
                        setResult(FINISH_ACTIVITY,intent);
                        finish();
                    }
                }
            });

    private void getLinkViews() {
        btOK = findViewById(R.id.bt_ok);
        btResendCode = findViewById(R.id.bt_resend_code);
        edtVerCode = findViewById(R.id.edt_verife_code);
        edtPass = findViewById(R.id.edt_pass);
        edtConfirPass = findViewById(R.id.edt_confirm_pass);
        agcManager = new AGCManager();
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

    }

    @Override
    public void AfterCreateAccountFail(String e) {

    }

    @Override
    public void ResetPassSuccess() {
        resultLauncher.launch(new Intent(ResetPassActivity.this,ResetPassSuccessActivity.class));
    }

    @Override
    public void ResetPassFail(String e) {
        Toast.makeText(this, "Lỗi khi đổi mật khẩu:\n"+e, Toast.LENGTH_SHORT).show();
        Log.e(TAG,""+e);
    }
}