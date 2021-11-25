package com.hqdang.baitaplonversion1.DangNhap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.hqdang.baitaplonversion1.TaoTaiKhoan.PersonInforActivity;
import com.huawei.agconnect.auth.AGConnectAuth;

import java.util.List;

import com.hqdang.baitaplonversion1.DuLieu.AGCManager;
import com.hqdang.baitaplonversion1.DuLieu.FbManeger;
import com.hqdang.baitaplonversion1.ManHinhChinh.AdminActivity;
import com.hqdang.baitaplonversion1.ManHinhChinh.UserActivity;
import com.hqdang.baitaplonversion1.Model.Staff;
import com.hqdang.baitaplonversion1.QuenMatKhau.EnterEmailActivity;
import com.hqdang.baitaplonversion1.R;

public class LogInActivity extends AppCompatActivity implements AGCManager.SigInCallBack , FbManeger.FBCallBack {
    String TAG = "LogInActivity";
    static final int FINISH_ACTIVITY = 1001;
    static final int RESULT_NEW_PASS = 2002;

    TextInputLayout tilEmail, tilPassword;
    EditText edtEmail, edtPassword;
    Button btForgetPassword, btGo;
    String uEmail, uPass;

    CheckBox cbTermsAndServices;
    TextView txtTermsAndServices;

    ProgressDialog progressDialog;

    AGCManager agcManager;
    FbManeger fbManeger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        getLinkViews();

        agcManager.addSigInCallBack(LogInActivity.this);

        getControls();
    }
//
    private void getControls() {
        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromView();
//                checkConditions.addAccountCallBack(LogInActivity.this);
//                checkConditions.checkEmail(uEmail, CheckConditions.AccountCallBack.EMAIL);
                if (!checkData(uEmail) && !checkData(uPass))
                    if (cbTermsAndServices.isChecked()){
                        progressDialog = ProgressDialog.show(LogInActivity.this, "Sign In",
                                "Vui lòng chờ !!!", true);
                        progressDialog.show();
                        agcManager.signInWitEmailAndPassword(uEmail,uPass);
                    }else
                        openDialogTermsAndServices();

              //  -----------------------------------------------------------------
                   //check mail
//                    if (uEmail.trim().length() == 0) {
//                    tilEmail.setError("Bạn cần nhập Email!");
//                }
//                else{
//                    tilEmail.setError(null);
//                }
//                //check pass
//                if (uPass.trim().length() == 0) {
//                    tilPassword.setError("Bạn cần nhập Password!");
//                }
//                else{
//                    tilPassword.setError(null);
//                }


                // check full

//               else {
//                    tilEmail.setError("TestDemo");
//                    Toast.makeText(LogInActivity.this, "Không được để trống ", Toast.LENGTH_SHORT).show();
//                }

               // set điều kiện login
//                else if (uEmail.trim().length() == 0 && uPass.trim().length() == 0) {
//                    tilEmail.setError("TestDemo");
//               }
//              else if (uEmail.trim().length() == 0) {
//                   tilEmail.setError("TestDemo");
//               }
//                else if (uPass.trim().length() == 0) {
//                    Toast.makeText(LogInActivity.this, "Bạn cần nhập Password!.", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        btForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultLauncher.launch(new Intent(LogInActivity.this, EnterEmailActivity.class));
                //Toast.makeText(LogInActivity.this, "Quen mat khau", Toast.LENGTH_SHORT).show();
            }
        });

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                tilPassword.setError(null);
            }
        });

        txtTermsAndServices.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        txtTermsAndServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://huadang27.github.io/dieukhoan/?fbclid=IwAR0pH4AotfreROu3YkuPW4uOWi9W208RHGRE8N16AGsF10ZsY8tI4fHRypg")));
            }
        });

//        btCreate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LogInActivity.this, PersonInforActivity.class));
//                //Toast.makeText(LogInActivity.this, "Tao tai khoan", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private boolean checkData(String abc){
        if (abc.isEmpty() || abc == null){
            return true;
        }else{
            return false;
        }
    }


    private void getDataFromView() {
        uEmail = edtEmail.getText().toString().trim();
        uPass = edtPassword.getText().toString().trim();
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == FINISH_ACTIVITY){
                        uEmail = (String) result.getData().getExtras().get("email");
                        uPass = (String) result.getData().getExtras().get("pass");
                        edtEmail.setText(uEmail);
                        edtPassword.setText(uPass);
                        //finish();
                    }
                }
            });

    // khi nhan thoat ra khoi man hinh
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if((keyCode ==KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder alertDialogBuiler = new AlertDialog.Builder(this);
            alertDialogBuiler.setMessage("Bạn có muốn thoát ứng dụng?");
            alertDialogBuiler.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    finish();
                }
            });
            alertDialogBuiler.setNegativeButton("No", new DialogInterface.OnClickListener() {@Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
            });
            AlertDialog alertDialog = alertDialogBuiler.create();
            alertDialog.show();
            return true;
        }else if ((keyCode==KeyEvent.KEYCODE_MENU)){
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    private void getLinkViews() {
        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        //btCreate = findViewById(R.id.bt_create);
        cbTermsAndServices = findViewById(R.id.cb_the_terms_and_services);
        txtTermsAndServices = findViewById(R.id.txt_the_terms_and_services);
        btForgetPassword = findViewById(R.id.bt_forget_password);
        btGo = findViewById(R.id.bt_go);
        agcManager = new AGCManager();
        fbManeger = new FbManeger();
    }

    @Override
    public void SinginSuccess(String id) {
        Log.e(TAG,"Sig In Success");
        fbManeger.addCallBacks(LogInActivity.this);
        fbManeger.QueryStaffInFo("dbUser",id);
    }

    @Override
    public void SinginFail(String e) {
        progressDialog.dismiss();
        Toast.makeText(this, "Sig In Fail: \n"+e, Toast.LENGTH_SHORT).show();
        Log.e(TAG,"Sig In Fail: \n"+e);
    }
// kiểm tra quyền admin, user khi đăng nhập thủ công
    @Override
    public void QueryStaffInFoSuccess(Staff staff) {
        progressDialog.dismiss();
        Log.e(TAG,"Query Success: "+staff.getId());
        if (staff.getStaff()){
            if (staff.getAccessRight()){
                startActivity(new Intent(LogInActivity.this, AdminActivity.class));
                finish();
            }else {
                startActivity(new Intent(LogInActivity.this, UserActivity.class));
                finish();
            }
        }else{
            Toast.makeText(this, "No staff members", Toast.LENGTH_SHORT).show();
            AGConnectAuth.getInstance().signOut();
        }
    }

    @Override
    public void QueryStaffInFoFail(String e) {
        progressDialog.dismiss();
        Toast.makeText(this, "Query Fail: "+e, Toast.LENGTH_SHORT).show();
        Log.e(TAG,"Query Fail: "+e);
    }

    @Override
    public void QueryCalenderSuccess(int off, int work, List<String> list) {

    }

    // ẩn bàn phím khi nhấn ngoài mành hình
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    private void openDialogTermsAndServices(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        new AlertDialog.Builder(LogInActivity.this)
                .setIcon(R.drawable.ic_error_dialog)
                .setTitle("Terms And Services")
                .setMessage("Bạn cần đồng ý với các điều khoản và dịch vụ để sử dụng ứng dụng này.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cbTermsAndServices.setChecked(true);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNeutralButton("Details", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://huadang27.github.io/dieukhoan/?fbclid=IwAR0pH4AotfreROu3YkuPW4uOWi9W208RHGRE8N16AGsF10ZsY8tI4fHRypg")));
                    }
                })
                .setCancelable(true)
                .show();

    }
}