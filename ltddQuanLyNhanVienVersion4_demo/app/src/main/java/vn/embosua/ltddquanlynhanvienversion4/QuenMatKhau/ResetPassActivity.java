package vn.embosua.ltddquanlynhanvienversion4.QuenMatKhau;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import vn.embosua.ltddquanlynhanvienversion4.DieuKien.CheckFormat;
import vn.embosua.ltddquanlynhanvienversion4.DieuKien.FormatCallBack;
import vn.embosua.ltddquanlynhanvienversion4.DuLieu.AGCManager;
import vn.embosua.ltddquanlynhanvienversion4.Model.PortraitActivity;
import vn.embosua.ltddquanlynhanvienversion4.R;
import vn.embosua.ltddquanlynhanvienversion4.TaoTaiKhoan.CreatePassActivity;

import static com.huawei.agconnect.auth.VerifyCodeSettings.ACTION_REGISTER_LOGIN;
import static com.huawei.agconnect.auth.VerifyCodeSettings.ACTION_RESET_PASSWORD;

public class ResetPassActivity extends PortraitActivity implements AGCManager.CreateAccountCallBack , FormatCallBack {
    String TAG = "ResetPassActivity";
    static final int FINISH_ACTIVITY = 1001;

    TextInputLayout tilVerCode, tilPass, tilConfirmPass;
    ImageView imgBack;
    Button btOK, btResendCode;
    EditText edtVerCode, edtPass, edtConfirPass;

    String uEmail, uVerCode, uPass, uConfirPass;

    AGCManager agcManager;
    CheckFormat checkFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        getLinkViews();

        agcManager.addCallBacks(ResetPassActivity.this);

        Intent intent = getIntent();
        uEmail = intent.getStringExtra("email");

        getControls();
    }

    private void getControls() {
        // tho??t
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // g???i d??? li???u ??i
        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromView();
                checkFormat.addCallBack(ResetPassActivity.this);
                if (checkFormat.checkVercodeAndPass(uVerCode,VERIFI,uPass,PASSWORD,uConfirPass,CONFIRMPASSWORD)){
                    agcManager.resetPasswordWithEemail(uEmail,uPass,uVerCode);
                }

            }
        });

        // g???i l???i m?? x??c nh???n
        btResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agcManager.sendConfirmCode(uEmail,ACTION_RESET_PASSWORD);
            }
        });
    }

    // ki???m tra pass c?? gi???ng nhau kh??ng
    private boolean checkConfirPass() {
        if (uPass.equals(uConfirPass))
            return true;
        else {
            Toast.makeText(ResetPassActivity.this, "Passwords are not the same !", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // ki???m tra d??? li???u nh???p c?? tr???ng kh??ng
    private boolean checkDataInfor(){
        if (checkData(uConfirPass) || checkData(uPass) || checkData(uConfirPass)){
            Toast.makeText(ResetPassActivity.this, "Not be empty", Toast.LENGTH_SHORT).show();
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

    // l???y d??? li???u tr??n m??n h??nh
    private void getDataFromView() {
        uVerCode = edtVerCode.getText().toString().trim();
        uConfirPass = edtConfirPass.getText().toString().trim();
        uPass = edtPass.getText().toString().trim();
    }

    // g???i d??? li???u ??i (?????n login )
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

    // li??n k???t v???i c??c ?????i t?????ng tr??n m??n h??nh
    private void getLinkViews() {
        tilVerCode = findViewById(R.id.til_VerCode);
        tilPass = findViewById(R.id.til_password);
        tilConfirmPass = findViewById(R.id.til_confirm_password);
        imgBack = findViewById(R.id.img_back);
        btOK = findViewById(R.id.bt_ok);
        btResendCode = findViewById(R.id.bt_resend_code);
        edtVerCode = findViewById(R.id.edt_verife_code);
        edtPass = findViewById(R.id.edt_pass);
        edtConfirPass = findViewById(R.id.edt_confirm_pass);
        agcManager = new AGCManager();
        checkFormat = new CheckFormat();
    }

    // ???n b??n ph??m khi nh???n ngo??i m??nh h??nh
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    @Override // sau khi g???i m?? x??c nh???n th??nh c??ng
    public void AfterSendSuccess() {
        Toast.makeText(this, "Code sent to Email.", Toast.LENGTH_SHORT).show();
        Log.e(TAG,"???? g???i m?? code v??o Email.");
    }

    @Override // n???u g???i m?? x??c nh???n th???t b???i
    public void AfterSendFail(String e) {
        Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        Log.e(TAG,"L???i khi g???i m?? x??c nh???n:"+e);
    }

    @Override
    public void AfterCreateAccountSuccess() {

    }

    @Override
    public void AfterCreateAccountFail(String e) {
        Log.e(TAG,e);
    }

    @Override
    public void ResetPassSuccess() {
        resultLauncher.launch(new Intent(ResetPassActivity.this,ResetPassSuccessActivity.class));
    }

    @Override
    public void ResetPassFail(String e) {
        Toast.makeText(this, "Error when changing password:\n"+e, Toast.LENGTH_SHORT).show();
        Log.e(TAG,""+e);
    }

    @Override
    public void FormatTrue(String check) {
//        switch (check){
//            case VERIFI: tilVerCode.setHelperText(null); break;
//            case PASSWORD: tilPass.setHelperText(null); break;
//            case CONFIRMPASSWORD: tilConfirmPass.setHelperText(null); break;
//        }
    }

    @Override
    public void FormatFail(String messenger, String check) {
//        switch (check){
//            case VERIFI: tilVerCode.setHelperText(messenger); break;
//            case PASSWORD: tilPass.setHelperText(messenger); break;
//            case CONFIRMPASSWORD: tilConfirmPass.setHelperText(messenger); break;
//        }
    }
}