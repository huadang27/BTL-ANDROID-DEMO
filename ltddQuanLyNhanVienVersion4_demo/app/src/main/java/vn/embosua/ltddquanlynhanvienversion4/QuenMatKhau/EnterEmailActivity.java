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

import vn.embosua.ltddquanlynhanvienversion4.DangNhap.LogInActivity;
import vn.embosua.ltddquanlynhanvienversion4.DieuKien.CheckFormat;
import vn.embosua.ltddquanlynhanvienversion4.DieuKien.FormatCallBack;
import vn.embosua.ltddquanlynhanvienversion4.DuLieu.AGCManager;
import vn.embosua.ltddquanlynhanvienversion4.Model.PortraitActivity;
import vn.embosua.ltddquanlynhanvienversion4.R;

import static com.huawei.agconnect.auth.VerifyCodeSettings.ACTION_RESET_PASSWORD;

public class EnterEmailActivity extends PortraitActivity implements FormatCallBack , AGCManager.CreateAccountCallBack {
    String TAG = "EnterEmailActivity";

    static final int FINISH_ACTIVITY = 1001;
    static final int RESULT_NEW_PASS = 2002;

    ImageView imgBack;
    Button btNext;
    EditText edtEmail;
    String uEmail;

    TextInputLayout tilEmail;
    CheckFormat checkFormat;
    AGCManager agcManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_email);

        getLinkViews();

        getControls();
    }

    private void getControls() {
        // thoát
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // tiếp tục
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uEmail = edtEmail.getText().toString().trim();
                checkFormat.addCallBack(EnterEmailActivity.this);
                if (checkFormat.checkEmail(uEmail,EMAIL)){
                    agcManager.addCallBacks(EnterEmailActivity.this);
                    agcManager.sendConfirmCode(uEmail,ACTION_RESET_PASSWORD);
                }

            }
        });
    }

    // liên kếtkết các đốượng hiển thị trên view
    private void getLinkViews() {
        imgBack = findViewById(R.id.img_back);
        btNext = findViewById(R.id.bt_next);
        edtEmail = findViewById(R.id.edt_email);
        tilEmail = findViewById(R.id.til_email);
        checkFormat = new CheckFormat();
        agcManager = new AGCManager();
    }

    // gửi dữ liệu đi (đến login )
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == FINISH_ACTIVITY){
                        //staff = (Staff) result.getData().getExtras().get("staff");
                        String email = (String) result.getData().getExtras().get("email");
                        String pass = (String) result.getData().getExtras().get("pass");
                        Intent intent = new Intent();
                        intent.putExtra("email",email);
                        intent.putExtra("pass",pass);
                        setResult(FINISH_ACTIVITY,intent);
                        finish();
                    }
                }
            });

    // ẩn bàn phím khi nhấn ngoài mành hình
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    @Override
    public void FormatTrue(String check) {
        //tilEmail.setHelperText(null);
    }

    @Override
    public void FormatFail(String messenger, String check) {
        //tilEmail.setHelperText(messenger);
    }

    @Override // sau khi gửi mã xác nhận thành công
    public void AfterSendSuccess() {
        Toast.makeText(this, "Code sent to Email.", Toast.LENGTH_SHORT).show();
        Log.e(TAG,"Đã gửi mã code vào Email.");
        Intent intent = new Intent(EnterEmailActivity.this,ResetPassActivity.class);
        intent.putExtra("email",uEmail);
        resultLauncher.launch(intent);
    }

    @Override
    public void AfterSendFail(String e) {
        Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        Log.e(TAG,"Lối khi gửi mã xác nhận:"+e);
    }

    @Override
    public void AfterCreateAccountSuccess() {

    }

    @Override
    public void AfterCreateAccountFail(String e) {

    }

    @Override
    public void ResetPassSuccess() {

    }

    @Override
    public void ResetPassFail(String e) {

    }
}