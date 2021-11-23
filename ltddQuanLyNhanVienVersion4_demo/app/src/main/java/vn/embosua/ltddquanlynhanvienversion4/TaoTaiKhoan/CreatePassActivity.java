package vn.embosua.ltddquanlynhanvienversion4.TaoTaiKhoan;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.huawei.agconnect.auth.AGConnectAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import vn.embosua.ltddquanlynhanvienversion4.ChiTiet.EditInforActivity;
import vn.embosua.ltddquanlynhanvienversion4.DangNhap.LogInActivity;
import vn.embosua.ltddquanlynhanvienversion4.DangNhap.SilentLoginActivity;
import vn.embosua.ltddquanlynhanvienversion4.DieuKien.CheckFormat;
import vn.embosua.ltddquanlynhanvienversion4.DieuKien.FormatCallBack;
import vn.embosua.ltddquanlynhanvienversion4.DuLieu.AGCManager;
import vn.embosua.ltddquanlynhanvienversion4.DuLieu.FbManeger;
import vn.embosua.ltddquanlynhanvienversion4.ManHinhChinh.UserActivity;
import vn.embosua.ltddquanlynhanvienversion4.Model.EditHistory;
import vn.embosua.ltddquanlynhanvienversion4.Model.PortraitActivity;
import vn.embosua.ltddquanlynhanvienversion4.Model.Staff;
import vn.embosua.ltddquanlynhanvienversion4.R;

import static com.huawei.agconnect.auth.VerifyCodeSettings.ACTION_REGISTER_LOGIN;

public class CreatePassActivity extends PortraitActivity implements AGCManager.CreateAccountCallBack,
        AGCManager.SigInCallBack, FbManeger.FBCallBack, FormatCallBack {
    private String TAG = "CreatePassActivity";

    static final int FINISH_ACTIVITY = 1001;

    TextInputLayout tilVercode, tilPass, tilConfirmPass;
    ImageView imgBack;
    EditText edtVerifeCode, edtPass, edtConfirmPass;
    Button btOK, btResendCode;

    String uPass, uConfirPass, uVercode, uEmail;
    Staff staff;

    AGCManager agcManager;
    FbManeger fbManeger;
    
    String uid, upass, uemail;

    CheckFormat checkFormat;

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

    // nhận thông tin nhân viên đã nhập từ bên person infor
    private void getDataStaff() {
        Intent intent = getIntent();
        staff = (Staff) intent.getSerializableExtra("staff");
        uEmail = staff.getEmail();
    }


    private void getControls() {
        //thoát
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // gửi dữ liệu đi
        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dieu kien thanh cong
                // neu thanh cong thi finish() activity nay va cac activity phia truoc
                getDataFromView();
                checkFormat.addCallBack(CreatePassActivity.this);
                //checkDataInfor() && checkConfirPass()
                if (checkFormat.checkVercodeAndPass(uVercode,VERIFI,uPass,PASSWORD,uConfirPass,CONFIRMPASSWORD)){
                    setDataStaff();

                    agcManager.creatWithEmail(uEmail, uVercode, uPass,"dbUser", staff);
                }

            }
        });

        // gửi lại mã xác nhận
        btResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agcManager.sendConfirmCode(staff.getEmail(),ACTION_REGISTER_LOGIN);
            }
        });
    }

    // kiểm tra dữ liệu có bỏ trống không
    private boolean checkConfirPass() {
        if (uPass.equals(uConfirPass))
            return true;
        else {
            Toast.makeText(CreatePassActivity.this, "Passwords are not the same !", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // set dư liệu cho nhân viên từ các giá trị lấy được trên view
    private void setDataStaff() {
        staff.setPassword(uPass);
        staff.setStaff(true);
    }

    // kiểm tra dữ liệu có bỏ trống không
    private boolean checkDataInfor(){
        if (checkData(uConfirPass) || checkData(uPass) || checkData(uConfirPass)){
            Toast.makeText(CreatePassActivity.this, "Not be empty", Toast.LENGTH_SHORT).show();
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
        uVercode = edtVerifeCode.getText().toString().trim();
        uConfirPass = edtConfirmPass.getText().toString().trim();
        uPass = edtPass.getText().toString().trim();
    }

    // liên các đối tượng hiển thị trên view
    private void getLinkViews() {
        tilVercode = findViewById(R.id.til_VerCode);
        tilPass = findViewById(R.id.til_password);
        tilConfirmPass = findViewById(R.id.til_confirm_password);
        imgBack = findViewById(R.id.img_back);
        edtVerifeCode = findViewById(R.id.edt_verife_code);
        edtPass = findViewById(R.id.edt_pass);
        edtConfirmPass = findViewById(R.id.edt_confirm_pass);
        btOK = findViewById(R.id.bt_ok);
        btResendCode = findViewById(R.id.bt_resend_code);
        staff = new Staff();
        agcManager = new AGCManager();
        fbManeger = new FbManeger();
        checkFormat = new CheckFormat();
    }

    // lưu lịch sử tạo tài khoản
    private void saveHistory(String reason) {
        // dinh dang thoi gian
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm");
        // tao lich su
        EditHistory history = new EditHistory();
        history.setDateTime(simpleDateFormat.format(new Date().getTime()));
        history.setIDStaff(staff.getId());
        history.setIDEditor(uid);
        history.setReason(reason);
        history.setInfoCorrected(staff);
        // luu lich su
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String random = databaseReference.child("dbHistory").push().getKey();
        databaseReference.child("dbHistory").child(random).setValue(history);
        finish();
    }

    // ẩn bàn phím
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }

    @Override // nếu gửi mã xác nhận thành công
    public void AfterSendSuccess() {
        Toast.makeText(this, "Code sent to Email.", Toast.LENGTH_SHORT).show();
        Log.e(TAG,"Đã gửi mã code vào Email.");
    }

    @Override // nếu gửi mã xác nhận thất bại
    public void AfterSendFail(String e) {
        Toast.makeText(this, "Way when sending confirmation code:\n"+e, Toast.LENGTH_SHORT).show();
        Log.e(TAG,"Lối khi gửi mã xác nhận: "+e);
    }

    @Override // nếu tạo tài khoản thành công
    public void AfterCreateAccountSuccess() {
        // đóng các activity phía trước
        setResult(FINISH_ACTIVITY);
        Toast.makeText(CreatePassActivity.this, "Success", Toast.LENGTH_SHORT).show();
        // lưu lịch sử
        saveHistory("create01101000");
        // tránh văng tài khoản
        AGConnectAuth.getInstance().signOut();
        agcManager.addSigInCallBack(CreatePassActivity.this);
        agcManager.signInWitEmailAndPassword(uemail,upass);

        //startActivity(new Intent(CreatePassActivity.this, SilentLoginActivity.class));
        //finish();
    }

    @Override // nếu tạo tài khoản thất bại báo lỗi
    public void AfterCreateAccountFail(String e) {
        if (e.equals(" code: 203818130 message: The user has been registered")){
            Toast.makeText(this, "The user has been registered", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }

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

    // kiem tra dieu kien
    @Override
    public void FormatTrue(String check) {
        switch (check){
            case VERIFI: tilVercode.setHelperText(null); break;
            case PASSWORD: tilPass.setHelperText(null); break;
            case CONFIRMPASSWORD: tilConfirmPass.setHelperText(null); break;
        }
    }

    @Override
    public void FormatFail(String messenger, String check) {
        switch (check){
            case VERIFI: tilVercode.setHelperText(messenger); break;
            case PASSWORD: tilPass.setHelperText(messenger); break;
            case CONFIRMPASSWORD: tilConfirmPass.setHelperText(messenger); break;
        }
    }

}